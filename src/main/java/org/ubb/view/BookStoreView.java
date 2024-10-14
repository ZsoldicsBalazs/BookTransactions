package org.ubb.view;

import org.ubb.domain.Client;
import org.ubb.domain.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BookStoreView {

    private Scanner scanner;
    public BookStoreView() {
        scanner = new Scanner(System.in);
    }

    public ViewMenuItems runMenu() {
        Arrays.stream(ViewMenuItems.values())
                .map(ViewMenuItems::getLabel)
                .forEach(System.out::println);


        ViewMenuItems selectedOption = ViewMenuItems.values()[scanner.nextInt()];
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
        System.out.println("First name:");
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
}
