package org.ubb.service;

import org.ubb.domain.Book;
import org.ubb.repository.Repository;

import java.util.List;
import java.util.Optional;

public class BookService {
    Repository<Integer, Book> bookRepository;

    public BookService(Repository<Integer, Book> bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> addBook(Book book) {
        return bookRepository.save(book);
    }
    public Optional<Book> getBook(int id) {
        return bookRepository.findOne(id);
    }
    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }
    public Optional<Book> updateBook(Book book) {
        return bookRepository.update(book);
    }
    public Optional<Book> deleteBook(int id) {
        return bookRepository.delete(id);
    }
}
