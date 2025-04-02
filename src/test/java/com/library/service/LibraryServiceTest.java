package com.library.service;

import com.library.model.Book;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        libraryService = new LibraryService(bookRepository, patronRepository);
    }

    @Test
    void addBook_ShouldSaveAndReturnBook() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book savedBook = libraryService.addBook(book);

        // Assert
        assertNotNull(savedBook);
        assertEquals(book.getTitle(), savedBook.getTitle());
        verify(bookRepository).save(book);
    }

    @Test
    void findBookByIsbn_ShouldReturnBook_WhenExists() {
        // Arrange
        String isbn = "978-0547928227";
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", isbn, 1937);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        // Act
        Optional<Book> foundBook = libraryService.findBookByIsbn(isbn);

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals(isbn, foundBook.get().getIsbn());
    }

    @Test
    void findBooksByTitle_ShouldReturnMatchingBooks() {
        // Arrange
        String titleQuery = "Hobbit";
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        when(bookRepository.findByTitleContaining(titleQuery))
                .thenReturn(Arrays.asList(book));

        // Act
        List<Book> foundBooks = libraryService.findBooksByTitle(titleQuery);

        // Assert
        assertFalse(foundBooks.isEmpty());
        assertEquals(1, foundBooks.size());
        assertTrue(foundBooks.get(0).getTitle().contains(titleQuery));
    }

    @Test
    void checkoutBook_ShouldCreateLendingRecord() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(patronRepository.findById(any())).thenReturn(Optional.of(patron));
        when(bookRepository.save(any())).thenReturn(book);
        when(patronRepository.save(any())).thenReturn(patron);

        // Act
        LendingRecord record = libraryService.checkoutBook(book.getId(), patron.getId());

        // Assert
        assertNotNull(record);
        assertEquals(book, record.getBook());
        assertEquals(patron, record.getPatron());
        assertFalse(book.isAvailable());
        verify(bookRepository).save(book);
        verify(patronRepository).save(patron);
    }

    @Test
    void checkoutBook_ShouldThrowException_WhenBookNotAvailable() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        book.setAvailable(false);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(patronRepository.findById(any())).thenReturn(Optional.of(patron));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> 
            libraryService.checkoutBook(book.getId(), patron.getId())
        );
    }

    @Test
    void returnBook_ShouldUpdateBookAndLendingRecord() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        book.setAvailable(false);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        LendingRecord lendingRecord = libraryService.checkoutBook(book.getId(), patron.getId());
        patron.addLendingRecord(lendingRecord);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(patronRepository.findById(any())).thenReturn(Optional.of(patron));
        when(bookRepository.save(any())).thenReturn(book);
        when(patronRepository.save(any())).thenReturn(patron);

        // Act
        libraryService.returnBook(book.getId(), patron.getId());

        // Assert
        assertTrue(book.isAvailable());
        verify(bookRepository).save(book);
        verify(patronRepository).save(patron);
    }
}