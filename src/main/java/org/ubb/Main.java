package org.ubb;

import org.ubb.controller.BookStoreController;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
import org.ubb.repository.BookStoreRepository;
import org.ubb.repository.BookStoreRepositoryImpl;
import org.ubb.service.ClientService;
import org.ubb.service.TransactionService;
import org.ubb.view.BookStoreView;
import org.ubb.view.ViewMenuItems;

public class Main {
    public static void main(String[] args) {

        BookStoreView bookStoreView = new BookStoreView();
        BookStoreRepository<Transaction> transactionRepository = new BookStoreRepositoryImpl<>();
        TransactionService transactionService = new TransactionService(transactionRepository);
        BookStoreRepository<Client> clientRepository = new BookStoreRepositoryImpl<>();
        ClientService clientService = new ClientService(clientRepository);
        BookStoreController bookStoreController = new BookStoreController(transactionService, bookStoreView, clientService);
        ViewMenuItems selectedItem = ViewMenuItems.FILTER_TRANSACTIONS;


        while (selectedItem != ViewMenuItems.EXIT) {
            selectedItem = bookStoreView.runMenu();
            bookStoreController.selectedOption(selectedItem);
        }


    }
}