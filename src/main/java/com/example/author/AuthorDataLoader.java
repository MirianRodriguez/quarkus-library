package com.example.author;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class AuthorDataLoader {

    private final AuthorRepository authorRepository;

    public AuthorDataLoader(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    void load(@Observes StartupEvent event) {

        authorRepository.persist(
            new Author("George", "Orwell")
        );

        authorRepository.persist(
            new Author("Agatha", "Christie")
        );

        authorRepository.persist(
            new Author("J.K.", "Rowling")
        );
    }
}