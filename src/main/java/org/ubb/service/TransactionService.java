package org.ubb.service;

import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
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
        // TODO transaction validation
        transactionRepository.save(transaction);
        return transaction;
    }

    public Optional<Transaction> findTransactionById(int id) {
        return transactionRepository.findOne(id);
    }


//    public List<Transaction> findTransactionByClient(Client client) {
//        return transactionRepository.findAll().stream()
//                .filter(transaction -> transaction.getClient().equals(client))
//                .collect(Collectors.toList());
//    }
//    public List<Transaction> findTransactionByBook(Book searchedBook) {
//        return transactionRepository.findAll().stream()
//                .filter(book -> book.getClient().equals(searchedBook))
//                .collect(Collectors.toList());
//    }

//    public boolean updatetrasnaction(Transaction newTransaction) {
//        return transactionRepository.update(newTransaction);
//    }

    public Optional<Transaction> deleteTransaction(Transaction transaction) {
        return transactionRepository.delete(transaction.getId());
    }

    public List<Transaction> getAll() {
        return StreamSupport
                .stream(transactionRepository.findAll().spliterator(), false)
                .toList();
    }
}
