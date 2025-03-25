package com.library.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LendingRecordTest {
    @Test
    void createLendingRecord_ShouldSetAllProperties() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);

        // Act
        LendingRecord record = new LendingRecord(book, patron, borrowDate, dueDate);

        // Assert
        assertNotNull(record.getId());
        assertEquals(book, record.getBook());
        assertEquals(patron, record.getPatron());
        assertEquals(borrowDate, record.getBorrowDate());
        assertEquals(dueDate, record.getDueDate());
        assertNull(record.getReturnDate());
        assertFalse(record.isReturned());
    }

    @Test
    void isOverdue_ShouldReturnTrue_WhenDueDatePassed() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        LocalDate borrowDate = LocalDate.now().minusDays(15);
        LocalDate dueDate = borrowDate.plusDays(14);
        LendingRecord record = new LendingRecord(book, patron, borrowDate, dueDate);

        // Act & Assert
        assertTrue(record.isOverdue());
    }

    @Test
    void isOverdue_ShouldReturnFalse_WhenBookReturned() {
        // Arrange
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        LocalDate borrowDate = LocalDate.now().minusDays(15);
        LocalDate dueDate = borrowDate.plusDays(14);
        LendingRecord record = new LendingRecord(book, patron, borrowDate, dueDate);

        // Act
        record.setReturnDate(LocalDate.now());

        // Assert
        assertFalse(record.isOverdue());
        assertTrue(record.isReturned());
    }
}