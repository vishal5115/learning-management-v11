package com.library.observer;

import com.library.model.Book;
import com.library.model.Patron;

public interface LendingObserver {
    void onBookBorrowed(Book book, Patron patron);
    void onBookReturned(Book book, Patron patron);
    void onBookOverdue(Book book, Patron patron);
}