package org.ubb.view;

public enum ViewMenuItems {


    ADD_CLIENT("Add nem client", 1),
    READ_CLIENT("Show clients", 2),
    UPDATE_CLIENT("Update client", 3),
    SELL_BOOKS("Creat a new transaction", 4),
    SEE_ALL_TRANSACTIONS("Show all transactions", 5),
    FILTER_TRANSACTIONS("Show transactions by criteria", 6),
    DELETE_TRANSACTION("Delete an existing transaction" ,7),
    ADD_BOOK("Add a new book", 8),
    SEE_ALL_BOOKS("Show all books", 9),
    EXIT("Exit", 10);

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
