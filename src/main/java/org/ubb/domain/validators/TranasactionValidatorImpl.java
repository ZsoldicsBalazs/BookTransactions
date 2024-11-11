package org.ubb.domain.validators;

import org.ubb.domain.Transaction;

public class TranasactionValidatorImpl implements Validator<Transaction> {
    @Override
    public void validate(Transaction transaction) throws ValidatorException {
        isClientIdValid(transaction);
        isTotalAmountValid(transaction);
        isSoldBooksIdsValid(transaction);
    }

    private void isClientIdValid(Transaction transaction) {
        if (transaction.getClientId() == null || transaction.getClientId() <= 0) {
            throw new ValidatorException("The client ID is invalid");
        }
    }

    private void isTotalAmountValid(Transaction transaction) {
        if (transaction.getTotalAmount() <= 0) {
            throw new ValidatorException("The total amount is invalid");
        }
    }

    private void isSoldBooksIdsValid(Transaction transaction) {
        if (transaction.getSoldBookId() == null) {
            throw new ValidatorException("Sold books list is empty");
        }
    }
}
