package com.gostack.lowes.lowesAssignment.service;

import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);
    BookDTO getBookById(Long id);
    Page<BookDTO> getAllBooks(int page, int size, String sort);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);
    Page<BookDTO> searchByTitle(String title, int page, int size, String sort);
    Page<BookDTO> searchByAuthorName(String authorName, int page, int size, String sort);
    Page<BookDTO> getBooksByAuthor(Long authorId, int page, int size, String sort);
}
