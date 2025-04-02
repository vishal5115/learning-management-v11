package com.library.observer;

import com.library.model.Book;
import com.library.model.Patron;

public class EmailNotifier implements LendingObserver {
    @Override
    public void onBookBorrowed(Book book, Patron patron) {
        System.out.println("Sending email to " + patron.getEmail() + 
            ": You have borrowed '" + book.getTitle() + "'. Due date: " + 
            java.time.LocalDate.now().plusDays(book.getLoanPeriod()));
    }

    @Override
    public void onBookReturned(Book book, Patron patron) {
        System.out.println("Sending email to " + patron.getEmail() + 
            ": Thank you for returning '" + book.getTitle() + "'");
    }

    @Override
    public void onBookOverdue(Book book, Patron patron) {
        System.out.println("Sending email to " + patron.getEmail() + 
            ": The book '" + book.getTitle() + "' is overdue!");
    }
}