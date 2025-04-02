package com.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void createBook_ShouldSetAllProperties() {
        // Arrange & Act
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);

        // Assert
        assertNotNull(book.getId());
        assertEquals("The Hobbit", book.getTitle());
        assertEquals("J.R.R. Tolkien", book.getAuthor());
        assertEquals("978-0547928227", book.getIsbn());
        assertEquals(1937, book.getPublicationYear());
        assertTrue(book.isAvailable());
    }

    @Test
    void equals_ShouldReturnTrue_WhenISBNsMatch() {
        // Arrange
        Book book1 = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Book book2 = new Book("The Hobbit (Different Edition)", "J.R.R. Tolkien", "978-0547928227", 1937);

        // Act & Assert
        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenISBNsDiffer() {
        // Arrange
        Book book1 = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Book book2 = new Book("The Hobbit", "J.R.R. Tolkien", "978-0547928228", 1937);

        // Act & Assert
        assertNotEquals(book1, book2);
    }
}