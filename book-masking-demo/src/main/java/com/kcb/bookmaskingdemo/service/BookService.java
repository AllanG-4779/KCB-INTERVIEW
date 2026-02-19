package com.kcb.bookmaskingdemo.service;

import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.dto.FetchRequest;

import java.util.List;

public interface BookService {

    BookDto createBook(BookDto bookDto);

    List<BookDto> getBooks(FetchRequest request);

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, BookDto bookDto);

    boolean deleteBook(Long id);

}
