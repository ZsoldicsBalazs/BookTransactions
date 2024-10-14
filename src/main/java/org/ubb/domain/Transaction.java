package org.ubb.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Transaction extends BaseEntity{

    private int id;
    private static AtomicInteger uniqueId = new AtomicInteger();
    private List<Book> soldBooks;
    private Client client;
    private double totalAmount;

    public Transaction(List<Book> books, Client client) {
        id = uniqueId.getAndIncrement();
        soldBooks = books;
        this.client = client;
        calculateTotalAmount();
    }

    public void calculateTotalAmount() {
        this.totalAmount = soldBooks.stream()
                .map(Book::getPrice)
                .reduce((double) 0, Double::sum);
    }

    public List<Book> getSoldBooks() {
        return soldBooks;
    }

    public void setSoldBooks(List<Book> soldBooks) {
        this.soldBooks = soldBooks;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", soldBooks=" + soldBooks +
                ", client=" + client +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
