package com.gostack.lowes.lowesAssignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gostack.lowes.lowesAssignment.DTO.BookDTO;
import com.gostack.lowes.lowesAssignment.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn("1234567890");
        bookDTO.setAuthorId(1L);
        bookDTO.setPublishedDate(LocalDate.now());

        given(bookService.createBook(any(BookDTO.class))).willReturn(bookDTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void searchBooks_ShouldReturnPagedResults() throws Exception {
        Page<BookDTO> page = new PageImpl<>(Collections.singletonList(new BookDTO()));
        given(bookService.searchByTitle(anyString(), anyInt(), anyInt(), anyString()))
                .willReturn(page);

        mockMvc.perform(get("/api/books/search?title=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void deleteBook_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
