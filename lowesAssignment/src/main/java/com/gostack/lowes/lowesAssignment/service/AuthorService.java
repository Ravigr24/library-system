package com.gostack.lowes.lowesAssignment.service;

import com.gostack.lowes.lowesAssignment.DTO.AuthorDTO;
import org.springframework.data.domain.Page;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO getAuthorById(Long id);
    Page<AuthorDTO> getAllAuthors(int page, int size, String sort);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    void deleteAuthor(Long id);
    Page<AuthorDTO> searchAuthors(String name, int page, int size, String sort);
}
