package com.example.book;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class BookFormDto {
    @FormParam("title")
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @FormParam("authorsIds")
    @Size(min = 1, message = "Debe haber al menos un autor")
    private List<Long> authorsIds;

    @FormParam("publicationYear")
    private int publicationYear;

    @FormParam("genreId") 
    @Nullable
    private Long genreId;
}