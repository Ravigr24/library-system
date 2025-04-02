package com.gostack.lowes.lowesAssignment.controller;

import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "Operations pertaining to books in Library Management System")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping
    @Operation(summary = "Get all books with pagination and sorting")
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {
        Page<BookDTO> books = bookService.getAllBooks(page, size, sort);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title or author name")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        if (title != null) {
            return ResponseEntity.ok(bookService.searchByTitle(title, page, size, sort));
        } else if (author != null) {
            return ResponseEntity.ok(bookService.searchByAuthorName(author, page, size, sort));
        } else {
            return ResponseEntity.ok(bookService.getAllBooks(page, size, sort));
        }
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get books by author ID")
    public ResponseEntity<Page<BookDTO>> getBooksByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {
        Page<BookDTO> books = bookService.getBooksByAuthor(authorId, page, size, sort);
        return ResponseEntity.ok(books);
    }
}
