package com.library.repository;

import com.library.model.Patron;
import java.util.*;

public class PatronRepository {
    private final Map<String, Patron> patrons = new HashMap<>();

    public Patron save(Patron patron) {
        patrons.put(patron.getId(), patron);
        return patron;
    }

    public Optional<Patron> findById(String id) {
        return Optional.ofNullable(patrons.get(id));
    }

    public Optional<Patron> findByEmail(String email) {
        return patrons.values().stream()
                .filter(patron -> patron.getEmail().equals(email))
                .findFirst();
    }

    public void delete(String id) {
        patrons.remove(id);
    }

    public List<Patron> findAll() {
        return new ArrayList<>(patrons.values());
    }
}