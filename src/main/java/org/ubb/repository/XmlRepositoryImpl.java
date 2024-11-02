package org.ubb.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ubb.domain.BaseEntity;
import org.ubb.domain.Client;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

public class XmlRepositoryImpl<ID, Entity extends BaseEntity<ID>> extends InMemoryRepositoryImpl<ID, Entity>{

    private final Validator<Entity> validator;
    private final Path filePath;
    private final Class<Entity> entityClass;
    private final Logger logger = LoggerFactory.getLogger(XmlRepositoryImpl.class);


    public XmlRepositoryImpl(String fileName, Class<Entity> clazz, Validator<Entity> validator) {
        super(validator);
        this.validator = validator;
        this.filePath = Path.of(fileName);
        this.entityClass = clazz;
        readXmlFile();
    }



    @Override
    public Optional<Entity> save(Entity entity) throws ValidatorException {
        super.save(entity);
        writeXmlFile(entity);
        readXmlFile();
        logger.info("New entity saved");
        return Optional.empty();
    }


    @Override
    public Optional<Entity> delete(ID id) {
        Optional<Entity> deletedItem =  super.delete(id);
        deleteEntityFromXml(id);
        logger.info("An entity was deleted");
        return deletedItem;
    }




    private void readXmlFile() {
        try {
            Element element = openXmlDocumentsRoot();

            NodeList rowDataNodes =  element.getChildNodes();

            for (int i = 0; i < rowDataNodes.getLength(); i++) {
                if(rowDataNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element rowData = (Element) rowDataNodes.item(i);

                Entity newEntity = entityClass.getDeclaredConstructor().newInstance();

                StreamSupport.stream(Arrays.stream(entityClass.getDeclaredFields()).spliterator(), false)
                        .forEach(field -> {
                            field.setAccessible(true);
                            Class<?> dataType = field.getType();
                            tagsValueFromXML(field, rowData)
                                    .ifPresent(tagsContent -> {
                                        try {
                                            if (field.getName().equals("id")) {
                                                newEntity.setId((ID) convertByDataType(dataType, tagsContent));
                                            }
                                            field.set(newEntity, convertByDataType(dataType, tagsContent));
                                        } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                        field.setAccessible(false);
                            });
                        });

                super.save(newEntity);

            }

        } catch ( InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RepositoryException(e);
        }


    }


    private void writeXmlFile(Entity entity) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(this.filePath.toFile());
            Element root = document.getDocumentElement();

            Element newObjectElement = document.createElement(entityClass.getName().split("\\.")[3].toLowerCase());
            AtomicReference<Element> newPropertyElement = new AtomicReference<>();
            StreamSupport.stream(Arrays.stream(entityClass.getDeclaredFields()).spliterator(), false)
                    .forEach(field -> {
                        String fieldName = field.getName();
                        field.setAccessible(true);
                        try {
                            String fieldValue = String.valueOf(field.get(entity));
                            field.setAccessible(false);

                            newPropertyElement.set(document.createElement(fieldName));
                            // adding a text value to the node
                            newPropertyElement.get().appendChild(document.createTextNode(fieldValue));
                            newObjectElement.appendChild(newPropertyElement.get());
                        } catch (IllegalAccessException e) {
                            throw new RepositoryException(e);
                        }
                    });
            root.appendChild(newObjectElement);

            saveModificationsToXml(document);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RepositoryException(e);
        }

    }

    private void saveModificationsToXml(Document document) {
        // Write to XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            // Specify your local file path
            StreamResult result = new StreamResult(filePath.toFile());
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RepositoryException(e);
        }

    }

    private void deleteEntityFromXml(ID id) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(this.filePath.toFile());
            Element root = document.getDocumentElement();
            NodeList nodeList =  root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {

                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE
                        && nodeList.item(i).getFirstChild().getTextContent().equals(id.toString())) {
                    Node deleted = root.removeChild(nodeList.item(i));
                }
            }
            saveModificationsToXml(document);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RepositoryException(e);
        }

    }





    private Element openXmlDocumentsRoot() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document =  documentBuilder.parse(this.filePath.toFile());
            return document.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RepositoryException(e);
        }


    }

    private Object convertByDataType(Class<?> dataType, String valueFromFile) {
        if (dataType != String.class) {
            try {
                Method valueOf = dataType.getMethod("valueOf", String.class);
                return dataType.cast(valueOf.invoke(null, valueFromFile ));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RepositoryException(e);
            }
        }
        return valueFromFile;

    }

    private Optional<String> tagsValueFromXML(Field field, Element element) {
        NodeList tags = element.getChildNodes();
        for (int i = 0; i < tags.getLength(); i++) {

            if(tags.item(i).getNodeType() == Node.ELEMENT_NODE
                   && tags.item(i).getNodeName().equals(field.getName().toString())) {
                return Optional.of(tags.item(i).getTextContent());
            }
        }
        return Optional.empty();
    }


}
