package com.library.model;

public class ReferenceBook extends Book {
    private static final int LOAN_PERIOD = 7; // 1 week for reference books

    public ReferenceBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear, "Reference");
    }

    @Override
    public int getLoanPeriod() {
        return LOAN_PERIOD;
    }
}