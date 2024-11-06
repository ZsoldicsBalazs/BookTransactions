package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.RepoTYPE;
import org.ubb.domain.validators.Validator;

public class RepositoryFactory {

    public static <ID, T extends BaseEntity<ID>> Repository<ID, T> createRepository(
            Class<T> entityType, RepoTYPE type, String filePath, Validator validator) {
        switch (type) {
            case XML:
                return new XmlRepositoryImpl<>(filePath,entityType, validator);
            case FILE:
                return new FileRepositoryImpl<>(filePath,entityType,validator);
            case MEMORY:
                return new InMemoryRepositoryImpl<>(validator);
            case SQL_POSTGRES:
                return new PostgresRepositoryImpl<>(validator, entityType);
            default:
                throw new IllegalArgumentException("Unknown storage type: " + type);
        }
    }

    public static <ID, T extends BaseEntity<ID>> Repository<ID, T> createRepository(
            Class<T> entityType, RepoTYPE type, Validator validator) {
        switch (type) {
            case SQL_POSTGRES:
                return new PostgresRepositoryImpl<>(validator, entityType);
            default:
                throw new IllegalArgumentException("Unknown storage type: " + type);
        }
    }



}

