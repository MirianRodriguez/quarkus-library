package com.example.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class GenreDataLoader {

    private final GenreRepository genreRepository;

    public GenreDataLoader(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    void load(@Observes StartupEvent event) {

        genreRepository.persist(
            new Genre("Fantasía")
        );

        genreRepository.persist(
            new Genre("Ciencia Ficción")
        );

        genreRepository.persist(
            new Genre("Misterio")
        );
    }
}