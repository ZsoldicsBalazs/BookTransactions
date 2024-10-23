package org.ubb.controller;

import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;
import org.ubb.domain.validators.BookStoreException;
import org.ubb.service.BookService;
import org.ubb.service.ClientService;
import org.ubb.service.TransactionService;
import org.ubb.view.BookStoreView;
import org.ubb.view.ViewMenuItems;

import java.util.List;

public class BookStoreController {

    private final TransactionService transactionService;
    private final BookStoreView view;
    private final ClientService clientService;
    private final BookService bookService;

    public BookStoreController(TransactionService transactionService, BookStoreView view, ClientService clientService, BookService bookService) {
        this.transactionService = transactionService;
        this.view = view;
        this.clientService = clientService;
        this.bookService = bookService;
    }

    public void selectedOption(ViewMenuItems selectedItem) {
        try {
            switch (selectedItem) {
                case ViewMenuItems.SELL_BOOKS:
                    Transaction transaction = view.readTransaction();
                    //view.showTransaction(transactionService.createTransaction(transaction));
                    break;
                case ViewMenuItems.SEE_ALL_TRANSACTIONS:
                    break;
                case ViewMenuItems.READ_CLIENT:
                    List<Client> clientList = clientService.getAll();
                    view.showClients(clientList);
                    break;
                case ViewMenuItems.ADD_CLIENT:
                    Client client = view.readClient();
                    clientService.addClient(client);
                    break;
                case ViewMenuItems.UPDATE_CLIENT:
                    Client clientUpdateRequest = view.readClient();
                    clientService.updateClient(clientUpdateRequest);
                    break;
                case ViewMenuItems.DELETE_CLIENT:
                    int clientId = view.readClientId();
                    clientService.deleteClient(clientId);
                    break;
                case ViewMenuItems.ADD_BOOK:
                    Book book = view.readBook();
                    if (book != null) {
                        bookService.addBook(book);
                    }else break;
                case ViewMenuItems.SEE_ALL_BOOKS:
                    view.showBooks(bookService.getAllBooks());

            }
        } catch (BookStoreException bookStoreException) {
            view.showException(bookStoreException.getMessage(), bookStoreException.getStackTrace());
        }
    }
}
