package com.gostack.lowes.lowesAssignment.repository;

import com.gostack.lowes.lowesAssignment.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.author a WHERE a.name LIKE %:authorName%")
    Page<Book> findByAuthorNameContainingIgnoreCase(String authorName, Pageable pageable);

    Page<Book> findByAuthorId(Long authorId, Pageable pageable);
}

