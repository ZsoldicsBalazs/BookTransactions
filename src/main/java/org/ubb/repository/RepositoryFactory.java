package org.ubb.repository;

import org.ubb.domain.BaseEntity;
import org.ubb.domain.validators.Validator;

public class RepositoryFactory {

    public static <ID, T extends BaseEntity<ID>> Repository<ID, T> createRepository(
            Class<T> entityType, String storageType, String filePath, Validator validator) {
        switch (storageType.toLowerCase()) {
            case "xml":
                return new XmlRepositoryImpl<>(filePath,entityType, validator);
            case "file":
                return new FileRepositoryImpl<>(filePath,entityType,validator);
            case "memory":
                return new InMemoryRepositoryImpl<>(validator);
            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
}

