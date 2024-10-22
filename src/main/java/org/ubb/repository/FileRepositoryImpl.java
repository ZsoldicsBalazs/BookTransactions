package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FileRepositoryImpl<ID, Entity extends BaseEntity<ID>> implements Repository<ID, Entity> {

    private final Map<ID, Entity> entities;
    private final String fileName;
    private final Class<Entity> clazz;

    public FileRepositoryImpl(String fileName, Class<Entity> clazz) {
        this.entities =  new HashMap<>();
        this.fileName = fileName;
        this.clazz = clazz;
        readFile();
    }


    private void readFile() {
        Path path = Path.of(fileName);

        try (Stream<String> fileLinesStream = Files.lines(path)) {
            fileLinesStream
                .forEach(line -> {
                    List<String> lineData = List.of(line.split(";"));
                    Entity newEntity;
                    try {
                        // creating a new Entity instance
                        newEntity = clazz.newInstance();
                        int[] index = {0};

                        //accessing all fields of the given generic class
                        Stream.of(newEntity.getClass().getDeclaredFields()).forEach(field -> {
                            field.setAccessible(true);

                            Class<?> typeOfField = field.getType();

                            //accessing the corresponding data from the line for the field
                            String data = lineData.get(index[0]);

                            try {
                                // if the type of the field is not String converting the line data to corresponding type
                                if (typeOfField != String.class) {
                                    Method valueOf = typeOfField.getMethod("valueOf", String.class);
                                     var convertedData = typeOfField.cast(valueOf.invoke(null, data ));

                                     //setting the objects field value
                                    field.set(newEntity, convertedData);
                                } else {
                                    field.set(newEntity, data);
                                }

                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                            field.setAccessible(false);
                            index[0]++;
                        });
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    entities.put(newEntity.getId(), newEntity);
                });
        } catch (IOException | ValidatorException exception) {
            throw new RepositoryException(exception);
        }
    }


    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Entity> findOne(ID id) {
        return Optional.empty();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Entity> findAll() {
        return entities.values();
    }

    /**
     * Saves the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Entity> save(Entity entity) throws ValidatorException {
        return Optional.empty();
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Entity> delete(ID id) {
        return Optional.empty();
    }

    /**
     * Updates the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Entity> update(Entity entity) throws ValidatorException {
        return Optional.empty();
    }
}
