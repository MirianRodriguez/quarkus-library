package com.example.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class Genre {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }
}
