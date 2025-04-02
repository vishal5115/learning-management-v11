package com.library.model;

public class FictionBook extends Book {
    private static final int LOAN_PERIOD = 21; // 3 weeks for fiction books

    public FictionBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear, "Fiction");
    }

    @Override
    public int getLoanPeriod() {
        return LOAN_PERIOD;
    }
}