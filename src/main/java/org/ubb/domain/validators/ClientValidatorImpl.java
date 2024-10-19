package org.ubb.domain.validators;

import org.ubb.domain.Client;

public class ClientValidatorImpl implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        if (!isFirstNameValid(entity)
                || !isLastNameValid(entity)
                || !isAgeValid(entity)
                || !isAddressValid(entity)
                || !isEmailValid(entity)
        )
            throw new ValidatorException("Clinet is not valid");

    }


    private boolean isFirstNameValid(Client entity) {
        return !entity.getFirstName().isEmpty();
    }
    private boolean isLastNameValid(Client entity) {
        return !entity.getLastName().isEmpty();
    }
    private boolean isAgeValid(Client entity) {
        return entity.getAge() >= 12
                && entity.getAge() <120;
    }
    private boolean isAddressValid(Client entity) {
        return entity.getAddress().length() >= 5;
    }
    private boolean isEmailValid(Client entity) {
        return  entity.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}
