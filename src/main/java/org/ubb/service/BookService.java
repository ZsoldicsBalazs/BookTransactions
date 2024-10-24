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

    /**
     * Adds a book to a specified repository
     * @param book must not be null
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     */
    public Optional<Book> addBook(Book book) {return bookRepository.save(book);}

    /**
     * Find the entity with the given {@code id}.
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     */
    public Optional<Book> getBook(int id) {
        return bookRepository.findOne(id);
    }

    /**
     * Get all Books in a repository specified
     * @return {@code List<Book>}
     */
    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    /**
     * Updates the given entity.
     *
     * @param book  must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *         entity.
     */
    public Optional<Book> updateBook(Book book) {
        return bookRepository.update(book);
    }

    /**
     * Delete a book with the given {@code id}
     * @param id of type {@code int}
     * @return an {@code Optional} null if there is no entity with the given id, otherwise the removed entity..
     */
    public Optional<Book> deleteBook(int id) {
        return bookRepository.delete(id);
    }
}
