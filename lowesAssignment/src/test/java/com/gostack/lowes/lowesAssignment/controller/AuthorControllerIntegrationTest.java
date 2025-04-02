package com.gostack.lowes.lowesAssignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gostack.lowes.lowesAssignment.DTO.AuthorDTO;
import com.gostack.lowes.lowesAssignment.service.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");
        authorDTO.setBio("Test Bio");

        given(authorService.createAuthor(any(AuthorDTO.class))).willReturn(authorDTO);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Author"));
    }

    @Test
    void getAuthorById_ShouldReturnAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Test Author");

        given(authorService.getAuthorById(1L)).willReturn(authorDTO);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Author"));
    }

    @Test
    void getAllAuthors_ShouldReturnPagedResults() throws Exception {
        Page<AuthorDTO> page = new PageImpl<>(Collections.singletonList(new AuthorDTO()));
        given(authorService.getAllAuthors(anyInt(), anyInt(), anyString())).willReturn(page);

        mockMvc.perform(get("/api/authors?page=0&size=10&sort=name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}