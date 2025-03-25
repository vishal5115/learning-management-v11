package com.library.service;

import com.library.model.Book;
import com.library.model.LendingRecord;
import com.library.model.Patron;
import com.library.observer.LendingObserver;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final List<LendingObserver> observers;

    public LibraryService(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.observers = new ArrayList<>();
    }

    public void addObserver(LendingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LendingObserver observer) {
        observers.remove(observer);
    }

    private void notifyBookBorrowed(Book book, Patron patron) {
        for (LendingObserver observer : observers) {
            observer.onBookBorrowed(book, patron);
        }
    }

    private void notifyBookReturned(Book book, Patron patron) {
        for (LendingObserver observer : observers) {
            observer.onBookReturned(book, patron);
        }
    }

    private void notifyBookOverdue(Book book, Patron patron) {
        for (LendingObserver observer : observers) {
            observer.onBookOverdue(book, patron);
        }
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    public LendingRecord checkoutBook(String bookId, String patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for checkout");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(book.getLoanPeriod());
        LendingRecord record = new LendingRecord(book, patron, borrowDate, dueDate);
        
        patron.addLendingRecord(record);
        patronRepository.save(patron);

        notifyBookBorrowed(book, patron);

        return record;
    }

    public void returnBook(String bookId, String patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found"));

        Optional<LendingRecord> activeRecord = patron.getBorrowingHistory().stream()
                .filter(record -> record.getBook().getId().equals(bookId) && !record.isReturned())
                .findFirst();

        if (activeRecord.isEmpty()) {
            throw new IllegalStateException("No active lending record found for this book and patron");
        }

        LendingRecord record = activeRecord.get();
        record.setReturnDate(LocalDate.now());
        book.setAvailable(true);
        
        bookRepository.save(book);
        patronRepository.save(patron);

        notifyBookReturned(book, patron);

        if (record.isOverdue()) {
            notifyBookOverdue(book, patron);
        }
    }
}