package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.RepoTYPE;
import org.ubb.domain.validators.Validator;

public class RepositoryFactory {

    public static <ID, T extends BaseEntity<ID>> Repository<ID, T> createRepository(
            Class<T> entityType, RepoTYPE type, String filePath, Validator<T> validator) {
        return switch (type) {
            case XML -> new XmlRepositoryImpl<>(filePath, entityType, validator);
            case FILE -> new FileRepositoryImpl<>(filePath, entityType, validator);
            case MEMORY -> new InMemoryRepositoryImpl<>(validator);
            case SQL_POSTGRES -> new PostgresRepositoryImpl<>(validator,entityType);
            default -> throw new IllegalArgumentException("Unknown storage type: " + type);
        };
    }
}

