package org.ubb.view;

import org.ubb.domain.Book;
import org.ubb.domain.Client;
import org.ubb.domain.Transaction;

import java.util.*;

public class BookStoreView {

    private Scanner scanner;
    public BookStoreView() {
        scanner = new Scanner(System.in);
    }

    public ViewMenuItems runMenu() {
        Arrays.stream(ViewMenuItems.values())
                .map(ViewMenuItems::getLabel)
                .forEach(System.out::println);


        ViewMenuItems selectedOption = ViewMenuItems.values()[scanner.nextInt() - 1];
        System.out.println(selectedOption);
        return selectedOption;

    }


    public Transaction readTransaction() {
        return null;
    }

    public void showTransaction(Transaction transaction) {
        System.out.println(transaction.toString());
    }

    public Client readClient() {
        System.out.println("First name:");
        String firstName = scanner.next();
        System.out.println("Last name:");
        String lastName = scanner.next();
        System.out.println("Age:");
        int age = scanner.nextInt();
        System.out.println("Address:");
        String address = scanner.next();
        System.out.println("Email:");
        String email = scanner.next();

        System.out.println("ID:");
        int id = scanner.nextInt();

        Client client = new Client(id, firstName, lastName, age, address, email);
        return client;
    }


    public void showClients(List<Client> clientList) {
        clientList.stream().forEach( client -> System.out.println(client.toString()));
    }

    public Book readBook(){
        Book book = null;
        //    int id,String title, List<String> author, String publisher, int year, double price
        try {
            System.out.println("ID:");
            int id = scanner.nextInt();
            System.out.println("Title: ");
            String title = scanner.next();
            System.out.println("Author: ");
            String author = scanner.next();
            System.out.println("Publisher");
            String publisher = scanner.next();
            System.out.println("Year: ");
            int year = scanner.nextInt();
            System.out.println("Price: ");
            double price = scanner.nextDouble();
           book = new Book(id, title, List.of(author), publisher, year, price);

        }catch (NoSuchElementException  e){
            System.out.println("Try Again");
            System.out.println(e.getMessage());
        }
        return book;
    }
    public void showBooks(List<Book> bookList){
        bookList.stream().forEach(book -> System.out.println(book.toString()));
    }

    public void showException(String message, StackTraceElement[] stackTrace) {
        System.out.println(message);
        System.out.println(stackTrace);
    }

    public int readClientId() {
        System.out.printf("Plead enter the id of the client: ");
        return scanner.nextInt();
    }
}
