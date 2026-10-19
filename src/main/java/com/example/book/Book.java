package com.example.book;

import java.util.List;

import com.example.author.Author;
import com.example.genre.Genre;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter 
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToMany 
    private java.util.List<Author> authors;
    private int publicationYear;
    @ManyToOne 
    @Nullable 
    private Genre genre;

    public Book() {
    }

    public Book(String title, List<Author> authors, int publicationYear, Genre genre) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.genre = genre;
    }

    public String getAuthorsNames() {
        if (authors == null || authors.isEmpty()) {
            return "-";
        }
        return authors.stream()
                .map(Author::getFullName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("-");
    }
}