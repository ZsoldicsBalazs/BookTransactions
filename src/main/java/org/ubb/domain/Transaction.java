package org.ubb.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction extends BaseEntity<Integer>{

    private LocalDateTime transactionDate;
    private Integer soldBookId; // TODO: convert to list
    private Integer clientId;
    private Double totalAmount;

    public Transaction(int id, LocalDateTime transactionDate, Integer books, Integer clientId) {
        this.transactionDate = transactionDate;
        super.setId(id);
        soldBookId = books;
        this.clientId = clientId;
    }

    public Transaction() {}


    public Integer getSoldBookId() {
        return soldBookId;
    }

    public void setSoldBookId(Integer soldBooksIds) {
        this.soldBookId = soldBooksIds;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + super.getId() +
                ", transactionDate=" + transactionDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")) +
                ", soldBooks=" + soldBookId +
                ", client=" + clientId +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
