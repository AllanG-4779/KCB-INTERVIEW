package com.kcb.bookmaskingdemo.repository;

import com.kcb.bookmaskingdemo.mdels.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
