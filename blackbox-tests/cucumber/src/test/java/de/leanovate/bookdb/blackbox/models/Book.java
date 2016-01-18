package de.leanovate.bookdb.blackbox.models;

import java.util.ArrayList;
import java.util.List;

public class Book {
    public String id;
    public String title;
    public String summary;
    public String isbn;
    public List<Author> authors = new ArrayList<>();

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }
}
