package com.library.factory;

import com.library.model.Book;
import com.library.model.FictionBook;
import com.library.model.NonFictionBook;
import com.library.model.ReferenceBook;

public class BookFactory {
    public static Book createBook(String type, String title, String author, String isbn, int publicationYear) {
        return switch (type.toLowerCase()) {
            case "fiction" -> new FictionBook(title, author, isbn, publicationYear);
            case "non-fiction" -> new NonFictionBook(title, author, isbn, publicationYear);
            case "reference" -> new ReferenceBook(title, author, isbn, publicationYear);
            default -> throw new IllegalArgumentException("Unknown book type: " + type);
        };
    }
}