package com.example.book;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    private static final int PAGE_SIZE = 10;

    public PanacheQuery<Book> findPage(
            int page,
            String author,
            String genre) {

        // construimos la consulta jpql dinámicamente según los filtros proporcionados
        // """ permite escribir una cadena multilinea sin concatenar con + y sin necesidad de escapar caracteres especiales
        // WHERE 1 = 1 es una técnica común para simplificar la construcción de consultas dinámicas
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT b
            FROM Book b
            LEFT JOIN b.authors a
            WHERE 1 = 1
            """);

        // usamos un mapa para almacenar los parámetros de la consulta
        Map<String, Object> parameters = new HashMap<>();

        // si se proporciona un filtro de autor, agregamos una condición a la consulta y su parámetro al mapa
        if (author != null && !author.isBlank()) {
            jpql.append("""
                AND (
                    LOWER(a.name) LIKE :author
                    OR LOWER(a.lastName) LIKE :author
                )
                """);

            parameters.put("author", "%" + author.trim().toLowerCase() + "%");
        }

        // si se proporciona un filtro de género, agregamos una condición a la consulta y su parámetro al mapa
        if (genre != null && !genre.isBlank()) {
            jpql.append(" AND b.genre.name LIKE :genre");
            parameters.put("genre", "%" + genre.trim() + "%");
        }

        // ordenamos los resultados por id de libro en orden descendente
        jpql.append(" ORDER BY b.id DESC");

        // creamos la consulta PanacheQuery con la jpql y los parámetros
        PanacheQuery<Book> query = find(jpql.toString(), parameters);

        // devolvemos la consulta paginada usando el objeto Page de Panache, ajustando el número de página para que sea al menos 0
        return query.page(Page.of(Math.max(0, page - 1), PAGE_SIZE));
    }
}