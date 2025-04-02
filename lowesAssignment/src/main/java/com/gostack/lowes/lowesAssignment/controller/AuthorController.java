package com.gostack.lowes.lowesAssignment.controller;

import com.gostack.lowes.lowesAssignment.DTO.AuthorDTO;
import com.gostack.lowes.lowesAssignment.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "Operations pertaining to authors in Library Management System")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO authorDTO = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDTO);
    }

    @GetMapping
    @Operation(summary = "Get all authors with pagination and sorting")
    public ResponseEntity<Page<AuthorDTO>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {
        Page<AuthorDTO> authors = authorService.getAllAuthors(page, size, sort);
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search authors by name")
    public ResponseEntity<Page<AuthorDTO>> searchAuthors(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {
        Page<AuthorDTO> authors = authorService.searchAuthors(name, page, size, sort);
        return ResponseEntity.ok(authors);
    }
}
