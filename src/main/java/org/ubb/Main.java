package org.ubb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ubb.controller.BookStoreController;
import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.RepoTYPE;
import org.ubb.domain.Transaction;
import org.ubb.domain.validators.BookValidatorImpl;
import org.ubb.domain.validators.ClientValidatorImpl;
import org.ubb.domain.validators.TranasactionValidatorImpl;
import org.ubb.domain.validators.Validator;
import org.ubb.repository.Repository;
import org.ubb.repository.RepositoryFactory;
import org.ubb.service.BookService;
import org.ubb.service.ClientService;
import org.ubb.service.TransactionService;
import org.ubb.view.BookStoreView;
import org.ubb.view.ViewMenuItems;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Application started");
        try {
            BookStoreView bookStoreView = new BookStoreView();


            Validator<Book> bookValidator = new BookValidatorImpl();
            Validator<Client> clientValidator = new ClientValidatorImpl();
            Validator<Transaction> transactionValidator = new TranasactionValidatorImpl();



            Repository<Integer,Book> bookRepository =
                    RepositoryFactory.createRepository(Book.class, RepoTYPE.SQL_POSTGRES,bookValidator);
            BookService bookService = new BookService(bookRepository);


            Repository<Integer,Transaction> transactionRepository =
                    RepositoryFactory.createRepository(Transaction.class, RepoTYPE.SQL_POSTGRES, transactionValidator);
            TransactionService transactionService = new TransactionService(transactionRepository);



            Repository<Integer, Client> clientRepository =
                    RepositoryFactory.createRepository(Client.class,RepoTYPE.SQL_POSTGRES,clientValidator);
            ClientService clientService = new ClientService(clientRepository);



            BookStoreController bookStoreController =
                    new BookStoreController(transactionService, clientService, bookService, bookStoreView);


            ViewMenuItems selectedItem = ViewMenuItems.FILTER_TRANSACTIONS;
            while (selectedItem != ViewMenuItems.EXIT) {
                selectedItem = bookStoreView.runMenu();
                bookStoreController.selectedOption(selectedItem);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));

        }
    }



}