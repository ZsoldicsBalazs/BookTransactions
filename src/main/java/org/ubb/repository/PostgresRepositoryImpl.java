package org.ubb.repository;

import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.Method;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return mapResultSetToObject(resultSet ,entityClass);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    private Optional<Entity> mapResultSetToObject(ResultSet resultSet, Class<Entity> entityClass) throws SQLException {
            try {
                Entity newEntity = entityClass.getDeclaredConstructor().newInstance();
                newEntity.setId((ID) resultSet.getObject("id", Integer.class));

                StreamSupport.stream(Arrays.stream(entityClass.getDeclaredFields()).spliterator(), false)
                        .forEach(field -> {
                            Class<?> dataType = field.getType();
                            try {
                                Method fieldSetter = entityClass
                                        .getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), dataType);
                                fieldSetter.invoke(newEntity, resultSet.getObject(field.getName(), dataType));

                            } catch (IllegalAccessException | SQLException | NoSuchMethodException |
                                     InvocationTargetException e) {
                                throw new RepositoryException(e);
                            }
                        });

                return Optional.of(newEntity);

            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RepositoryException(e.getMessage(), e.getCause());
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
                Optional<Entity> entity = mapResultSetToObject(resultSet, entityClass);
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
                            PreparedStatement statement = saveConnection.prepareStatement(
                                    "INSERT INTO client (firstname,lastname,age,address,email) SELECT ?,?,?,?,? " +
                                            "WHERE NOT EXISTS (SELECT 1 FROM client WHERE firstname = ? AND lastname =? AND email = ?)");
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
                                return Optional.empty();
                            }else {
                                logger.info("Client with firstname {} lastname {} email {} already exists", c.getFirstName(), c.getLastName(), c.getEmail());
                                return Optional.of(entity);
                            }



                        }
                        case Book b -> {
                            PreparedStatement statement = saveConnection.prepareStatement(
                                    "INSERT INTO book (title,author,publisher,year,price) SELECT ?,?,?,?,? " +
                                            "WHERE NOT EXISTS (SELECT 1 FROM book WHERE title = ? AND author = ? AND year = ?)");
                            statement.setString(1, b.getTitle());
                            statement.setString(2, b.getAuthor());
                            statement.setString(3, b.getPublisher());
                            statement.setInt(4, b.getYear());
                            statement.setDouble(5, b.getPrice());

                            statement.setString(6,b.getTitle());
                            statement.setString(7,b.getAuthor());
                            statement.setInt(8,b.getYear());

                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                logger.info("Book {} saved successfully, {} Rows Affected",b.getId(), rowsAffected);
                                return Optional.empty();
                            }else {
                                logger.info("Book with firstname {} lastname {} email {} already exists", b.getTitle(), b.getAuthor(), b.getYear());
                                return Optional.of(entity);
                            }

                        }
                        case Transaction t -> {
                            PreparedStatement statement = saveConnection.prepareStatement(
                                    "INSERT INTO transaction(soldbookid,clientid,transactiondate) SELECT ?,?,? " +
                                    "WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE clientid = ? AND transactiondate = ?)");

//                            Timestamp timestamp = Timestamp.valueOf(t.getTransactionDate());
                            statement.setInt(1, t.getSoldBookId());
                            statement.setInt(2, t.getClientId());
                            statement.setTimestamp(3,Timestamp.valueOf(t.getTransactionDate()));

                            //
                            statement.setInt(4,t.getClientId());
                            statement.setTimestamp(5,Timestamp.valueOf(t.getTransactionDate()));

                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                logger.info("Transaction, with ID{} saved successfully",t.getId());
                                return Optional.empty();
                            }
                            else {
                                logger.info("Transaction, with ID{} was not saved, already exists",t.getId());
                                return Optional.of(entity);
                            }
                        }


                        default -> throw new RepositoryException("Unknown entity type" + entity);
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

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToObject(resultSet, entityClass);
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
        validator.validate(entity);

        try(Connection connection = ConnectionPool.getConnection()) {
            switch (entity) {
                case Client c -> {
                    String sql = "UPDATE " + entityClass.getSimpleName() +
                            " SET firstname = ?, lastname = ?, age = ?, address = ?, email = ? WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1,c.getFirstName());
                    preparedStatement.setString(2,c.getLastName());
                    preparedStatement.setInt(3,c.getAge());
                    preparedStatement.setString(4,c.getAddress());
                    preparedStatement.setString(5,c.getEmail());
                    preparedStatement.setInt(6,c.getId());
                    preparedStatement.executeUpdate();
                }
                case Book b -> {
                    String sql = "UPDATE " + entityClass.getSimpleName() +
                            " SET title = ?, author = ?, publisher = ?, year = ?, price = ? WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1,b.getTitle());
                    preparedStatement.setString(2,b.getAuthor());
                    preparedStatement.setString(3,b.getPublisher());
                    preparedStatement.setInt(4,b.getYear());
                    preparedStatement.setDouble(5,b.getPrice());
                    preparedStatement.setInt(6,b.getId());
                    preparedStatement.executeUpdate();
                }

                default -> throw new RepositoryException("Unknown entity type" + entity);

            }
        }
        catch (SQLException e){
            throw new RepositoryException(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
