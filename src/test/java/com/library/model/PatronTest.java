package com.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatronTest {
    @Test
    void createPatron_ShouldSetAllProperties() {
        // Arrange & Act
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");

        // Assert
        assertNotNull(patron.getId());
        assertEquals("John Doe", patron.getName());
        assertEquals("john@example.com", patron.getEmail());
        assertEquals("123-456-7890", patron.getPhoneNumber());
        assertTrue(patron.getBorrowingHistory().isEmpty());
    }

    @Test
    void addLendingRecord_ShouldAddToHistory() {
        // Arrange
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        LendingRecord record = new LendingRecord(book, patron, java.time.LocalDate.now(), 
            java.time.LocalDate.now().plusDays(14));

        // Act
        patron.addLendingRecord(record);

        // Assert
        assertEquals(1, patron.getBorrowingHistory().size());
        assertEquals(record, patron.getBorrowingHistory().get(0));
    }

    @Test
    void getBorrowingHistory_ShouldReturnDefensiveCopy() {
        // Arrange
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        LendingRecord record = new LendingRecord(book, patron, java.time.LocalDate.now(), 
            java.time.LocalDate.now().plusDays(14));
        patron.addLendingRecord(record);

        // Act
        List<LendingRecord> history = patron.getBorrowingHistory();
        history.clear(); // Try to modify the returned list

        // Assert
        assertEquals(1, patron.getBorrowingHistory().size()); // Original list should be unchanged
    }
}