package com.library.model;

public class NonFictionBook extends Book {
    private static final int LOAN_PERIOD = 14; // 2 weeks for non-fiction books

    public NonFictionBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear, "Non-Fiction");
    }

    @Override
    public int getLoanPeriod() {
        return LOAN_PERIOD;
    }
}