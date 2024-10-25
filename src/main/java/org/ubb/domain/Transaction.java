package org.ubb.domain;

import java.util.List;

public class Transaction extends BaseEntity<Integer>{

    private Integer id;
    private Integer soldBooksIds; // TODO: convert to list
    private Integer clientId;
    private Double totalAmount;

    public Transaction(int id ,Integer books, Integer clientId) {
        super.setId(id);
        this.id = id;
        soldBooksIds = books;
        this.clientId = clientId;
    }

    public Transaction() {}


    public Integer getSoldBooksIds() {
        return soldBooksIds;
    }

    public void setSoldBooksIds(Integer soldBooksIds) {
        this.soldBooksIds = soldBooksIds;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", soldBooks=" + soldBooksIds +
                ", client=" + clientId +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
