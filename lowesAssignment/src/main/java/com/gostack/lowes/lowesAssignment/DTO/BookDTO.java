package com.gostack.lowes.lowesAssignment.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private String authorName;

    @PastOrPresent(message = "Published date must be in the past or present")
    private LocalDate publishedDate;
}
