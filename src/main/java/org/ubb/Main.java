package org.ubb;

import org.ubb.controller.BookStoreController;
import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
import org.ubb.domain.validators.BookValidatorImpl;
import org.ubb.domain.validators.ClientValidatorImpl;
import org.ubb.domain.validators.TranasactionValidatorImpl;
import org.ubb.domain.validators.Validator;
import org.ubb.repository.BookFileRepository;
import org.ubb.repository.BookStoreRepositoryImpl;
import org.ubb.repository.ClientFileRepository;
import org.ubb.repository.Repository;
import org.ubb.service.BookService;
import org.ubb.service.ClientService;
import org.ubb.service.TransactionService;
import org.ubb.view.BookStoreView;
import org.ubb.view.ViewMenuItems;

public class Main {

    public static void main(String[] args) {

        BookStoreView bookStoreView = new BookStoreView();


        Validator<Book> bookValidator = new BookValidatorImpl();
        Validator<Client> clientValidator = new ClientValidatorImpl();
        Validator<Transaction> transactionValidator = new TranasactionValidatorImpl();


        Repository<Integer, Book> bookRepository = new BookFileRepository(bookValidator,"dataFiles\\books.txt");
        BookService bookService = new BookService(bookRepository);

        Repository<Integer,Transaction> transactionRepository = new BookStoreRepositoryImpl<>(transactionValidator);
        TransactionService transactionService = new TransactionService(transactionRepository);

        Repository<Integer, Client> clientRepository = new ClientFileRepository(clientValidator, "dataFiles\\clients.txt");
        ClientService clientService = new ClientService(clientRepository);


        BookStoreController bookStoreController =
                new BookStoreController(transactionService, bookStoreView, clientService,bookService);

        ViewMenuItems selectedItem = ViewMenuItems.FILTER_TRANSACTIONS;


        while (selectedItem != ViewMenuItems.EXIT) {
            selectedItem = bookStoreView.runMenu();
            bookStoreController.selectedOption(selectedItem);
        }


    }
}