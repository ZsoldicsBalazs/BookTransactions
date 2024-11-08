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
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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
        List<Entity> entities = new ArrayList<>();
        String query = "SELECT * FROM " + entityClass.getSimpleName();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Optional<Entity> entity = maptResultSetToObject(resultSet, entityClass);
                entity.ifPresent(entities::add);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving entities: " + e.getMessage());
        }

        return entities;

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

                    switch (entity) {
                        case Client c -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO client (firstname,lastname,age,address,email) SELECT ?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM client WHERE firstname = ? AND lastname =? AND email = ?)");
                            statement.setString(1, c.getFirstName());
                            statement.setString(2, c.getLastName());
                            statement.setInt(3, c.getAge());
                            statement.setString(4, c.getAddress());
                            statement.setString(5, c.getEmail());

                            //NOT EXISTS PART
                            statement.setString(6,c.getFirstName());
                            statement.setString(7,c.getLastName());
                            statement.setString(8,c.getEmail());

                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                logger.info("Client {} saved successfully, {} Rows Affected",c.getId(), rowsAffected);
                            }else
                                logger.info("Client with firstname {} lastname {} email {} already exists", c.getFirstName(),c.getLastName(),c.getEmail());

                            return Optional.empty();



                        }
                        case Book b -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO book (title,author,publisher,year,price) VALUES (?,?,?,?,?)");
                            statement.setString(1, b.getTitle());
                            statement.setString(2, b.getAuthor());
                            statement.setString(3, b.getPublisher());
                            statement.setInt(4, b.getYear());
                            statement.setDouble(5, b.getPrice());
                            statement.executeUpdate();
                            logger.info("Book {} saved successfully",b.getId());
                            return Optional.empty();

                        }
                        case Transaction t -> {
                            PreparedStatement statement = saveConnection.prepareStatement("INSERT INTO transaction VALUES (?,?,?)");
                            statement.setInt(1, t.getSoldBooksIds());
                            statement.setInt(2, t.getClientId());
                            statement.setDouble(3, t.getTotalAmount());
                            statement.executeUpdate();
                            logger.info("Transaction, with ID{} saved successfully");
                            return Optional.empty();
                        }


                        default -> throw new RuntimeException("Unknown entity type" + entity);
                    }

        }

        catch(SQLException e){
            throw new RepositoryException(e.getMessage(), e);
        }

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
        String query = "DELETE FROM " + entityClass.getSimpleName() + " WHERE id = ? RETURNING *";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return maptResultSetToObject(resultSet, entityClass);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting entity with id " + id + ": " + e.getMessage());
        }

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
