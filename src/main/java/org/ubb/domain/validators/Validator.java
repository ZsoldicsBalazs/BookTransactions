package org.ubb.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
