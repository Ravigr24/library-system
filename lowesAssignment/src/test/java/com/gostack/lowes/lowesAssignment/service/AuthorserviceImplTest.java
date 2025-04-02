package com.gostack.lowes.lowesAssignment.service;

import com.gostack.lowes.lowesAssignment.DTO.AuthorDTO;
import com.gostack.lowes.lowesAssignment.entity.Author;
import com.gostack.lowes.lowesAssignment.exception.ResourceNotFoundException;
import com.gostack.lowes.lowesAssignment.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "J.K. Rowling", "Harry Potter author");
        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("J.K. Rowling");
        authorDTO.setBio("Harry Potter author");
    }

    @Test
    void createAuthor_ShouldReturnSavedAuthor() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.createAuthor(authorDTO);

        assertNotNull(result);
        assertEquals(authorDTO.getName(), result.getName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO result = authorService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals(author.getName(), result.getName());
    }

    @Test
    void getAuthorById_WhenAuthorNotExists_ShouldThrowException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getAuthorById(1L);
        });
    }

    @Test
    void updateAuthor_WhenAuthorExists_ShouldReturnUpdatedAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.updateAuthor(1L, authorDTO);

        assertNotNull(result);
        assertEquals(authorDTO.getName(), result.getName());
    }

    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteAuthor() {
        // Mock existsById to return true
        when(authorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAuthor_WhenAuthorNotExists_ShouldThrowException() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.deleteAuthor(1L);
        });
    }

    @Test
    void searchAuthors_ShouldReturnPagedResults() {
        Page<Author> page = new PageImpl<>(Collections.singletonList(author));
        when(authorRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<AuthorDTO> result = authorService.searchAuthors("Rowling", 0, 10, "name");

        assertEquals(1, result.getTotalElements());
        assertEquals("J.K. Rowling", result.getContent().get(0).getName());
    }
}