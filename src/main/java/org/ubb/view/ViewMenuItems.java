package org.ubb.view;

public enum ViewMenuItems {


    ADD_CLIENT("Add nem client", 1),
    READ_CLIENT("Show client", 2),
    SEE_ALL_CLIENTS("Show all clients", 3 ),
    UPDATE_CLIENT("Update client", 4),
    DELETE_CLIENT("Delete client", 5),
    SELL_BOOKS("Creat a new transaction", 6),
    SEE_ALL_TRANSACTIONS("Show all transactions", 7),
    FILTER_TRANSACTIONS("Show transactions by criteria", 8),
    DELETE_TRANSACTION("Delete an existing transaction" ,9),
    ADD_BOOK("Add a new book", 10),
    SEE_ALL_BOOKS("Show all books", 11),
    EXIT("Exit", 12);

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
