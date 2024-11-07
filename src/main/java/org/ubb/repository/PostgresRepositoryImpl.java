package org.ubb.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ubb.domain.BaseEntity;
import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
import org.ubb.domain.validators.RepositoryException;
import org.ubb.domain.validators.Validator;
import org.ubb.domain.validators.ValidatorException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import java.util.stream.StreamSupport;

public class PostgresRepositoryImpl<ID, Entity extends BaseEntity<ID>> implements Repository<ID, Entity>{


    private final Validator<Entity> validator;
    private final Class<Entity> entityClass;
    private final Logger logger = LoggerFactory.getLogger(PostgresRepositoryImpl.class);

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


    private Optional<Entity> maptResultSetToObject(ResultSet resultSet, Class<Entity> entityClass) throws SQLException {
            try {
                resultSet.next();
                System.out.println(resultSet.getInt(1));
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

            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RepositoryException(e.getMessage());
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
        validator.validate(entity);

        try (Connection saveConnection = ConnectionPool.getConnection()) {

            Optional<Entity> existingClient = findOne(entity.getId());

            if (existingClient.isEmpty()) {

                    switch (entity) {
                        case Client c -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO clients VALUES (?,?,?,?,?,?)");
                            statement.setInt(1, c.getId());
                            statement.setString(2, c.getFirstName());
                            statement.setString(3, c.getLastName());
                            statement.setInt(4, c.getAge());
                            statement.setString(5, c.getAddress());
                            statement.setString(6, c.getEmail());
                            statement.executeUpdate();
                            logger.info("Client {} saved successfully");
                            return Optional.empty();
                        }
                        case Book b -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO books VALUES (?,?,?,?,?,?)");
                            statement.setInt(1, b.getId());
                            statement.setString(2, b.getTitle());
                            statement.setString(3, b.getAuthor());
                            statement.setString(4, b.getPublisher());
                            statement.setInt(5, b.getYear());
                            statement.setDouble(6, b.getPrice());
                            statement.executeUpdate();
                            logger.info("Book {} saved successfully");
                            return Optional.empty();

                        }
                        case Transaction t -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO transactions VALUES (?,?,?,?,?,?)");
                            statement.setInt(1, t.getId());
                            statement.setInt(2, t.getSoldBooksIds());
                            statement.setInt(3, t.getClientId());
                            statement.setDouble(4, t.getTotalAmount());
                            statement.executeUpdate();
                            logger.info("Transaction, with ID{ {}} saved successfully");
                            return Optional.empty();
                        }


                        default -> throw new RuntimeException("Unknown entity type" + entity);
                    }
            }
        }

        catch(SQLException e){
            throw new RepositoryException(e.getMessage(), e);
        }

        logger.info("Entity already exists in database !");
        return Optional.of(entity);
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
