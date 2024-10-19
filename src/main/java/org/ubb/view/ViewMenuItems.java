package org.ubb.view;

public enum ViewMenuItems {


    ADD_CLIENT("0. Add nem client"),
    READ_CLIENT("1. Show clients"),
    SELL_BOOKS("2. Creat a new transaction"),
    SEE_ALL_TRANSACTIONS("3. Show all transactions"),
    FILTER_TRANSACTIONS("4. Show transactions by criteria"),
    DELETE_TRANSACTION("5. Delete an existing transaction"),
    ADD_BOOK("6. Add a new book"),
    SEE_ALL_BOOKS("7. Show all books"),
    EXIT("8. Exit");
    private final String label;

    private ViewMenuItems(String shownName) {
        this.label = shownName;
    }

    public String getLabel() {
        return this.label;
    }

}
