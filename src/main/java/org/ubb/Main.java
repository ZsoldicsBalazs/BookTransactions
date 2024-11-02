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
import org.ubb.repository.FileRepositoryImpl;
import org.ubb.repository.Repository;
import org.ubb.repository.RepositoryFactory;
import org.ubb.service.BookService;
import org.ubb.service.ClientService;
import org.ubb.service.TransactionService;
import org.ubb.view.BookStoreView;
import org.ubb.view.ViewMenuItems;

public class Main {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Application started");
        try {
            BookStoreView bookStoreView = new BookStoreView();


            Validator<Book> bookValidator = new BookValidatorImpl();
            Validator<Client> clientValidator = new ClientValidatorImpl();
            Validator<Transaction> transactionValidator = new TranasactionValidatorImpl();


//            Repository<Integer, Book> bookRepository =
//                    new FileRepositoryImpl<>("dataFiles/books.txt", Book.class, bookValidator);
            Repository<Integer,Book> bookRepository = RepositoryFactory.createRepository(Book.class, RepoTYPE.XML,"dataFiles/xml/books.xml",bookValidator);
            BookService bookService = new BookService(bookRepository);


            Repository<Integer,Transaction> transactionRepository =
                    new FileRepositoryImpl<>("dataFiles/transactions.txt",Transaction.class ,transactionValidator);

//            Repository<Integer,Transaction> transactionRepository = RepositoryFactory.createRepository(Transaction.class,RepoTYPE.XML,"dataFiles/xml/transaction.xml",transactionValidator);
            TransactionService transactionService = new TransactionService(transactionRepository);


//            Repository<Integer, Client> clientRepository =
//                    new FileRepositoryImpl<>("dataFiles/clients.txt", Client.class, clientValidator);
//            ClientService clientService = new ClientService(clientRepository);

//            Repository<Integer, Client> clientRepository =
//                    new XmlRepositoryImpl<>("dataFiles/clients.xml", Client.class, clientValidator);

            Repository<Integer, Client> clientRepository = RepositoryFactory.createRepository(Client.class,RepoTYPE.XML,"dataFiles/xml/clients.xml",clientValidator);
            ClientService clientService = new ClientService(clientRepository);




            BookStoreController bookStoreController =
                    new BookStoreController(transactionService, clientService, bookService, bookStoreView);

            ViewMenuItems selectedItem = ViewMenuItems.FILTER_TRANSACTIONS;


            while (selectedItem != ViewMenuItems.EXIT) {
                selectedItem = bookStoreView.runMenu();
                bookStoreController.selectedOption(selectedItem);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
}