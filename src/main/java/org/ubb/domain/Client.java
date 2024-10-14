package org.ubb.domain;

import java.util.ArrayList;
import java.util.List;

public class Client extends BaseEntity<Integer> {

    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private String email;
    private List<Book> books;

    public Client(int id, String firstName, String lastName, int age, String address, String email) {
        super.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.books = new ArrayList<Book>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void buyBook(Book book) {
        books.add(book);
    }
    public int getTotalBooks(){
        return books.size();
    }

    @Override
    public String toString() {
        return "Client{ ID: " +super.getId()  +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", books=" + books +
                '}';
    }
}
