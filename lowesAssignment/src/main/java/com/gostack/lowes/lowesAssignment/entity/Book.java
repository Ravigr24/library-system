package com.gostack.lowes.lowesAssignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @NotNull(message = "Published date is required")
    private LocalDate publishedDate;

    public Book(Long id, String title, String isbn, Author author, LocalDate publishedDate) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    public Book() {

    }
}
