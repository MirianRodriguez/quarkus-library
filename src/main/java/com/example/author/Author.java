package com.example.author;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String lastName;

    public Author() {
    }

    public Author(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public String getFullName() {
        return name + " " + lastName;
    }
}
