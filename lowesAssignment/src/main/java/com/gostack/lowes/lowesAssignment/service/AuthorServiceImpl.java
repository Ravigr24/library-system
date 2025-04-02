package com.gostack.lowes.lowesAssignment.service;


import com.gostack.lowes.lowesAssignment.DTO.AuthorDTO;
import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.entity.Author;
import com.gostack.lowes.lowesAssignment.entity.Book;
import com.gostack.lowes.lowesAssignment.exception.ResourceNotFoundException;
import com.gostack.lowes.lowesAssignment.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    public Page<AuthorDTO> getAllAuthors(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(this::convertToDTO);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        author.setName(authorDTO.getName());
        author.setBio(authorDTO.getBio());

        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }

    public Page<AuthorDTO> searchAuthors(String name, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Author> authors = authorRepository.findByNameContainingIgnoreCase(name, pageable);
        return authors.map(this::convertToDTO);
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setBio(author.getBio());

        // Converting books if needed
        if (author.getBooks() != null) {
            authorDTO.setBooks(author.getBooks().stream()
                    .map(this::convertBookToDTO)
                    .collect(Collectors.toList()));
        }

        return authorDTO;
    }

    private BookDTO convertBookToDTO(Book book) {
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

    private Author convertToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBio(authorDTO.getBio());
        return author;
    }
}