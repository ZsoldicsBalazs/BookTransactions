package org.ubb.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.ubb.domain.Book;
import org.ubb.repository.BookFileRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceTest {
    public static final Book BOOK = new Book(1, "titlu", List.of("autor1", "autor2"), "publishje", 1999, 200.00);
    static BookService bookService;
    static BookFileRepository repository;

    @BeforeAll
    static void setUp() {
        repository = mock(BookFileRepository.class);
        bookService = new BookService(repository);
    }

    @AfterAll
    static void tearDown() {
        bookService = null;
        repository = null;
    }

    @Test
    void addBook() {
        when(repository.save(ArgumentMatchers.any(Book.class))).thenReturn(Optional.of(BOOK));
        Optional<Book> newbook = bookService.addBook(BOOK);
        assertTrue(newbook.isPresent());
        assertEquals(BOOK, newbook.get());
    }

    @Test
    void getBook() {
    }

    @Test
    void getAllBooks() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }
}