package com.library.repository;

import com.library.model.Book;
import java.util.*;

public class BookRepository {
    private final Map<String, Book> books = new HashMap<>();

    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public Optional<Book> findByIsbn(String isbn) {
        return books.values().stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    public List<Book> findByTitleContaining(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    public List<Book> findByAuthorContaining(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }

    public void delete(String id) {
        books.remove(id);
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }
}