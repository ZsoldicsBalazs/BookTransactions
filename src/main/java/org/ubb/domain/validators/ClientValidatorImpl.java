package org.ubb.domain.validators;

import org.ubb.domain.Client;

public class ClientValidatorImpl implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        try {
            if (!isFirstNameValid(entity)
                    || !isLastNameValid(entity)
                    || !isAgeValid(entity)
                    || !isAddressValid(entity)
                    || !isEmailValid(entity)
            ){
                throw new ValidatorException("Invalid Client");
            }
        }
        catch (ValidatorException validatorException){
            throw new ValidatorException(validatorException.getMessage());
        }


    }


    private boolean isFirstNameValid(Client entity) {
        if(entity.getFirstName().isEmpty()){
            throw new ValidatorException("First name is empty");
        }
        return true;

    }
    private boolean isLastNameValid(Client entity) {
        if(entity.getLastName().isEmpty()){
            throw new ValidatorException("Last name is empty");
        }
        return true;
    }
    private boolean isAgeValid(Client entity) {
       if(!(entity.getAge() >= 12  && entity.getAge() <120)){
         throw new ValidatorException("Age must be between 12 and 120");
       }
       return true;
    }
    private boolean isAddressValid(Client entity) {
        if(!(entity.getAddress().length() >=5)){
            throw new ValidatorException("Address is empty");
        }
        return true;

    }
    private boolean isEmailValid(Client entity) {
        if(!entity.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")){
            throw new ValidatorException("Invalid email");
        }
       return true;
    }
}
