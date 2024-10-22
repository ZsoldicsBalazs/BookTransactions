package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
                        newEntity = clazz.getDeclaredConstructor(String.class, String.class).newInstance(lineData.get(0), lineData.get(1));
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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
