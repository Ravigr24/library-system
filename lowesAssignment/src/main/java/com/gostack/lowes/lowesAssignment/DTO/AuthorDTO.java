package com.gostack.lowes.lowesAssignment.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorDTO {
    private Long id;

    @NotBlank(message = "Author name is required")
    private String name;

    private String bio;

    private List<BookDTO> books = new ArrayList<>();
}
