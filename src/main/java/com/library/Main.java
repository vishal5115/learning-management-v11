package com.library;

import com.library.factory.BookFactory;
import com.library.model.Book;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.observer.EmailNotifier;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import com.library.service.LibraryService;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories
        BookRepository bookRepository = new BookRepository();
        PatronRepository patronRepository = new PatronRepository();
        
        // Initialize service with observer
        LibraryService libraryService = new LibraryService(bookRepository, patronRepository);
        libraryService.addObserver(new EmailNotifier());

        // Add some sample books using Factory pattern
        Book fictionBook = BookFactory.createBook("fiction", "The Hobbit", "J.R.R. Tolkien", "978-0547928227", 1937);
        Book nonFictionBook = BookFactory.createBook("non-fiction", "A Brief History of Time", "Stephen Hawking", "978-0553380163", 1988);
        Book referenceBook = BookFactory.createBook("reference", "Oxford Dictionary", "Oxford Press", "978-0199571123", 2010);

        libraryService.addBook(fictionBook);
        libraryService.addBook(nonFictionBook);
        libraryService.addBook(referenceBook);

        // Add a patron
        Patron patron = new Patron("John Doe", "john@example.com", "123-456-7890");
        patronRepository.save(patron);

        System.out.println("\n=== Testing Book Checkout ===");
        try {
            // Checkout a fiction book (21-day loan period)
            LendingRecord record = libraryService.checkoutBook(fictionBook.getId(), patron.getId());
            System.out.println("Fiction book checked out successfully: " + record.getBook().getTitle());
            System.out.println("Due date: " + record.getDueDate());
        } catch (Exception e) {
            System.err.println("Error checking out book: " + e.getMessage());
        }

        System.out.println("\n=== Testing Book Search ===");
        System.out.println("Books by Tolkien:");
        libraryService.findBooksByAuthor("Tolkien")
                .forEach(book -> System.out.println(book.getTitle() + " (" + book.getCategory() + ")"));

        System.out.println("\n=== Testing Book Return ===");
        try {
            libraryService.returnBook(fictionBook.getId(), patron.getId());
            System.out.println("Book returned successfully: " + fictionBook.getTitle());
        } catch (Exception e) {
            System.err.println("Error returning book: " + e.getMessage());
        }

        // Try to checkout a reference book (7-day loan period)
        System.out.println("\n=== Testing Reference Book Checkout ===");
        try {
            LendingRecord record = libraryService.checkoutBook(referenceBook.getId(), patron.getId());
            System.out.println("Reference book checked out successfully: " + record.getBook().getTitle());
            System.out.println("Due date: " + record.getDueDate() + " (shorter loan period)");
        } catch (Exception e) {
            System.err.println("Error checking out book: " + e.getMessage());
        }
    }
}