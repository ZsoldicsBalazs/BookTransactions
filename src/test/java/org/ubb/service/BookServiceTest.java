//package org.ubb.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.ubb.domain.Book;
//import org.ubb.repository.BookFileRepository;
//import org.ubb.repository.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class BookServiceTest {
//    public static final Book BOOK = new Book(1, "titlu", "autor1", "publishje", 1999, 200.00);
//
//
//    @Mock
//    public Repository repository;
//    @InjectMocks
//    static BookService bookService;
//
//    @BeforeAll
//    static void setUp() {
//        repository = mock(BookFileRepository.class);
//        bookService = new BookService(repository);
//    }
//
//    @AfterAll
//    static void tearDown() {
//        bookService = null;
//        repository = null;
//    }
//
//    @Test
//    void addBook() {
//        when(repository.save(ArgumentMatchers.any(Book.class))).thenReturn(Optional.of(BOOK));
//        Optional<Book> newbook = bookService.addBook(BOOK);
//        assertTrue(newbook.isPresent());
//        assertEquals(BOOK, newbook.get());
//    }
//
//    @Test
//    void getBook() {
//    }
//
//    @Test
//    void getAllBooks() {
//    }
//
//    @Test
//    void updateBook() {
//    }
//
//    @Test
//    void deleteBook() {
//    }
//}