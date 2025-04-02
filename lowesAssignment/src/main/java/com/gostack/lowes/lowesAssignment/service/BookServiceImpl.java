package com.gostack.lowes.lowesAssignment.service;

import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.entity.Author;
import com.gostack.lowes.lowesAssignment.entity.Book;
import com.gostack.lowes.lowesAssignment.exception.ResourceNotFoundException;
import com.gostack.lowes.lowesAssignment.repository.AuthorRepository;
import com.gostack.lowes.lowesAssignment.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDTO.getAuthorId()));

        Book book = convertToEntity(bookDTO);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDTO(book);
    }

    public Page<BookDTO> getAllBooks(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(this::convertToDTO);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + bookDTO.getAuthorId()));

        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setAuthor(author);
        book.setPublishedDate(bookDTO.getPublishedDate());

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    public Page<BookDTO> searchByTitle(String title, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        return books.map(this::convertToDTO);
    }

    public Page<BookDTO> searchByAuthorName(String authorName, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable);
        return books.map(this::convertToDTO);
    }

    public Page<BookDTO> getBooksByAuthor(Long authorId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> books = bookRepository.findByAuthorId(authorId, pageable);
        return books.map(this::convertToDTO);
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublishedDate(book.getPublishedDate());
        if (book.getAuthor() != null) {
            bookDTO.setAuthorId(book.getAuthor().getId());
            bookDTO.setAuthorName(book.getAuthor().getName());
        }
        return bookDTO;
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublishedDate(bookDTO.getPublishedDate());
        return book;
    }
}