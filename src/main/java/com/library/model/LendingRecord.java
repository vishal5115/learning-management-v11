package com.library.model;

import java.time.LocalDate;
import java.util.UUID;

public class LendingRecord {
    private final String id;
    private final Book book;
    private final Patron patron;
    private final LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;

    public LendingRecord(Book book, Patron patron, LocalDate borrowDate, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.book = book;
        this.patron = patron;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue() {
        return !isReturned() && LocalDate.now().isAfter(dueDate);
    }
}