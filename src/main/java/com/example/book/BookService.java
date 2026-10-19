package com.example.book;

import java.util.List;

import com.example.author.Author;
import com.example.author.AuthorRepository;
import com.example.common.PageResult;
import com.example.genre.Genre;
import com.example.genre.GenreRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, GenreRepository genreRepository,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.listAll();
    }

    public PageResult<Book> getBooksPage(int page, String author, String genre) {
        PanacheQuery<Book> query = bookRepository.findPage(page, author, genre);

        return new PageResult<>(
            query.list(),
            page,
            query.page().size,
            query.count(),
            query.pageCount()
        );
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void createBook(String title, List<Long> authorsIds, int publicationYear, Long genreId) {
        Genre genre = null;
        if (genreId != null) {
            genre = genreRepository.findById(genreId);
        }
        List<Author> authors = authorRepository.findByIds(authorsIds);
        Book book = new Book(title, authors, publicationYear, genre);

        bookRepository.persist(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id);
        if (book != null) {
            bookRepository.delete(book);
        }
    }

    @Transactional
    public void updateBook(Long id, String title, List<Long> authorsIds, int publicationYear, Long genreId) {
        Book book = bookRepository.findById(id);
        Genre genre = null;
        if (genreId != null) {
            genre = genreRepository.findById(genreId);
        }
        List<Author> authors = authorRepository.findByIds(authorsIds);
        if (book != null) {
            book.setTitle(title);
            book.setAuthors(authors);
            book.setPublicationYear(publicationYear);
            book.setGenre(genre);
            bookRepository.persist(book);
        }
    }
}