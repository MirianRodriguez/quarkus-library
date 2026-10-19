package com.example.genre;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class GenreRepository implements PanacheRepository<Genre> {
    
}
