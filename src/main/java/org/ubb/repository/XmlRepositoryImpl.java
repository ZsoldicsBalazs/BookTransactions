package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class XmlRepositoryImpl<ID, Enity extends BaseEntity<ID>> extends InMemoryRepositoryImpl<ID, Enity>{

    private final Validator<Enity> validator;
    private final Path filePath;
    private final Class<Enity> entityClass;



    public XmlRepositoryImpl(String fileName, Class<Enity> clazz, Validator<Enity> validator) {
        super(validator);
        this.validator = validator;
        this.filePath = Path.of(fileName);
        this.entityClass = clazz;
        readXmlFile();
    }





    private void readXmlFile() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document =  documentBuilder.parse(this.filePath.toFile());

            Element element = document.getDocumentElement();


            NodeList rowDataNodes =  element.getChildNodes();

            for (int i = 0; i < rowDataNodes.getLength(); i++) {
                if(rowDataNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element rowData = (Element) rowDataNodes.item(i);

                Enity newEntity = entityClass.getDeclaredConstructor().newInstance();

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

        } catch (ParserConfigurationException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    private Object convertByDataType(Class<?> dataType, String valueFromFile) {
        if (dataType != String.class) {
            try {
                Method valueOf = dataType.getMethod("valueOf", String.class);
                return dataType.cast(valueOf.invoke(null, valueFromFile ));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
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
