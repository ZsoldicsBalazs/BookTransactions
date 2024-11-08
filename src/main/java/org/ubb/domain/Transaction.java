package org.ubb.domain;

public class Transaction extends BaseEntity<Integer>{


    private Integer soldBooksIds; // TODO: convert to list
    private Integer clientId;
    private Double totalAmount;

    public Transaction(int id ,Integer books, Integer clientId) {
        super.setId(id);
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
                "id=" + super.getId() +
                ", soldBooks=" + soldBooksIds +
                ", client=" + clientId +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
