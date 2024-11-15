package org.ubb.service;

import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
import org.ubb.domain.validators.BookStoreException;
import org.ubb.domain.validators.ResourceNotFound;
import org.ubb.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TransactionService {

    private final Repository<Integer, Transaction> transactionRepository;

    public TransactionService(Repository<Integer, Transaction> transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transactionRepository.save(transaction).isPresent()) {
            throw new BookStoreException("Transaction isn't saved");
        }
        return transaction;
    }

    public Transaction findTransactionById(int id) {
        return transactionRepository
                .findOne(id)
                .orElseThrow(() -> new BookStoreException("Transaction not found"));
    }


    public List<Transaction> findTransactionByClient(Client client) {
        return StreamSupport.stream(transactionRepository.findAll().spliterator(), false)
                .filter(transaction -> transaction.getClientId().equals(client.getId()))
                .collect(Collectors.toList());
    }

    public List<Transaction> findTransactionByBook(Book searchedBook) {
        return StreamSupport.stream(transactionRepository.findAll().spliterator(), false)
                .filter(book -> book.getSoldBookId().equals(searchedBook.getId()))
                .collect(Collectors.toList());
    }

    public List<Transaction> getAll() {
        return StreamSupport
                .stream(transactionRepository.findAll().spliterator(), false)
                .toList();
    }


    public void updateTransaction(Transaction newTransaction) {
        if (transactionRepository.update(newTransaction).isPresent()) {
            throw new BookStoreException("Transaction isn't updated");
        }

    }

    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction.getId())
                .orElseThrow(() -> new ResourceNotFound("Transaction wit the given ID is not found therefore not deleted"));
    }


}
