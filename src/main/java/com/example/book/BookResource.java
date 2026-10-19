package com.example.book;

import java.net.URI;
import java.util.List;
import java.util.Set;

import com.example.author.Author;
import com.example.author.AuthorRepository;
import com.example.common.PageResult;
import com.example.common.Pagination;
import com.example.genre.Genre;
import com.example.genre.GenreRepository;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

@Path("/books")
public class BookResource {
    private final BookService bookService;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    @Inject
    Validator validator;

    public BookResource(BookService bookService, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance list(
                List<Book> books,
                List<Integer> pages,
                int currentPage,
                int totalPages,
                String author,
                String genre
        );

        public static native TemplateInstance detail(Book book);

        public static native TemplateInstance formBook(String action, BookFormDto form,
                Set<ConstraintViolation<BookFormDto>> violations, List<Genre> genres, List<Author> authors);
    }

    /* Endpoints que sirven páginas - Retornan un Template */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance list(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("author") String author,
            @QueryParam("genre") String genre
    ) {
        PageResult<Book> result = bookService.getBooksPage(page, author, genre);

        Pagination pagination = Pagination.create(
                result.currentPage(),
                result.totalPages()
        );

        return Templates.list(
                result.items(),
                pagination.pages(),
                pagination.currentPage(),
                pagination.totalPages(),
                author,
                genre
        );
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance detail(@PathParam("id") Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        return Templates.detail(book);
    }

    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance newBook() {
        return Templates.formBook("/books", new BookFormDto(), Set.of(), genreRepository.listAll(),
                authorRepository.listAll());
    }

    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam("id") Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        BookFormDto form = new BookFormDto();
        form.setTitle(book.getTitle());
        form.setAuthorsIds(book.getAuthors().stream().map(a -> a.getId()).toList());
        form.setPublicationYear(book.getPublicationYear());
        form.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        return Templates.formBook("/books/" + id, form, Set.of(), genreRepository.listAll(),
                authorRepository.listAll());
    }

    /* Endpoints que ejecutan acciones */

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response create(@BeanParam BookFormDto form) {
        Set<ConstraintViolation<BookFormDto>> violations = validator.validate(form);

        if (!violations.isEmpty()) {
            return Response.ok(Templates.formBook("/books", form, violations, genreRepository.listAll(),
                    authorRepository.listAll())).build();
        }
        bookService.createBook(form.getTitle(), form.getAuthorsIds(), form.getPublicationYear(), form.getGenreId());
        // redirecciona a la lista de libros
        return Response.seeOther(URI.create("/books")).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        bookService.deleteBook(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response update(
            @PathParam("id") Long id,
            @BeanParam BookFormDto form) {
        Set<ConstraintViolation<BookFormDto>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            return Response.ok(
                    Templates.formBook("/books/" + id, form, violations, genreRepository.listAll(),
                            authorRepository.listAll()))
                    .build();
        }
        bookService.updateBook(
                id,
                form.getTitle(),
                form.getAuthorsIds(),
                form.getPublicationYear(),
                form.getGenreId());
        return Response.seeOther(
                URI.create("/books")).build();
    }
}