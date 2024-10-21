package org.ubb.view;

public enum ViewMenuItems {


    ADD_CLIENT("Add nem client", 1),
    READ_CLIENT("Show clients", 2),
    UPDATE_CLIENT("Update client", 3),
    DELETE_CLIENT("Delete client", 4),
    SELL_BOOKS("Creat a new transaction", 5),
    SEE_ALL_TRANSACTIONS("Show all transactions", 6),
    FILTER_TRANSACTIONS("Show transactions by criteria", 7),
    DELETE_TRANSACTION("Delete an existing transaction" ,8),
    ADD_BOOK("Add a new book", 9),
    SEE_ALL_BOOKS("Show all books", 10),
    EXIT("Exit", 11);

    private final String label;
    private final int id;

    private ViewMenuItems(String shownName, int id) {
        this.label = shownName;
        this.id = id;
    }

    public String getLabel() {
        return "-- " + this.id + ". " + this.label;
    }

}
