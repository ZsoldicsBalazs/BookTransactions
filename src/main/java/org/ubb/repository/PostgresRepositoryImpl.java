package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class PostgresRepositoryImpl<ID, Entity extends BaseEntity<ID>> implements Repository<ID, Entity>{


    private final Validator<Entity> validator;
    private final Class<Entity> entityClass;
    private final ConnectionPool connectionPool = new ConnectionPool();

    public PostgresRepositoryImpl(Validator<Entity> validator, Class<Entity> entityClass) {
        this.validator = validator;
        this.entityClass = entityClass;
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
        try (Connection connection = ConnectionPool.getConnection()) {
            String query = "SELECT * FROM " + entityClass.getSimpleName() + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1,id);
            ResultSet resultSet = statement.executeQuery();
            return maptResultSetToObject(resultSet ,entityClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Optional<Entity> maptResultSetToObject(ResultSet resultSet, Class<Entity> entityClass) {
        while (true) {
            try {
                if (!resultSet.next()) {
                    Entity newEntity = entityClass.getDeclaredConstructor().newInstance();

                    StreamSupport.stream(Arrays.stream(entityClass.getDeclaredFields()).spliterator(), false)
                            .forEach(field -> {
                                field.setAccessible(true);
                                Class<?> dataType = field.getType();
                                try {
                                    if (field.getName().equals("id")) {
                                        newEntity.setId((ID) resultSet.getObject(field.getName(), dataType));
                                    }
                                    /* TODO to check if the conversion is performed properly */
                                    field.set(newEntity, resultSet.getObject(field.getName(), dataType));
                                } catch (IllegalAccessException | SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                field.setAccessible(false);
                            });

                    return Optional.of(newEntity);
                }
            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Entity> findAll() {
        //Beata
        return null;
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
        //Balazs
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
        //Bea
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
        //Balazs
        return Optional.empty();
    }
}
