package com.gostack.lowes.lowesAssignment.service;


import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.entity.Author;
import com.gostack.lowes.lowesAssignment.entity.Book;
import com.gostack.lowes.lowesAssignment.exception.ResourceNotFoundException;
import com.gostack.lowes.lowesAssignment.repository.AuthorRepository;
import com.gostack.lowes.lowesAssignment.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class bookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    private Book book;
    private BookDTO bookDTO;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "J.K. Rowling", "Harry Potter author");
        book = new Book(1L, "Harry Potter", "978-0747532743", author, LocalDate.parse("1997-06-26"));

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Harry Potter");
        bookDTO.setIsbn("978-0747532743");
        bookDTO.setAuthorId(1L);
        bookDTO.setPublishedDate(LocalDate.parse("1997-06-26"));
    }

    @Test
    void createBook_WhenAuthorExists_ShouldReturnSavedBook() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookServiceImpl.createBook(bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_WhenAuthorNotExists_ShouldThrowException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookServiceImpl.createBook(bookDTO);
        });
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookServiceImpl.getBookById(1L);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookServiceImpl.updateBook(1L, bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
    }

    @Test
    void searchByTitle_ShouldReturnPagedResults() {
        Page<Book> page = new PageImpl<>(Collections.singletonList(book));
        when(bookRepository.findByTitleContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<BookDTO> result = bookServiceImpl.searchByTitle("Potter", 0, 10, "title");

        assertEquals(1, result.getTotalElements());
        assertEquals("Harry Potter", result.getContent().get(0).getTitle());
    }
}
