package com.kcb.bookmaskingdemo.controller;

import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.dto.ResponseDto;
import com.kcb.bookmaskingdemo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ResponseDto> createBook(@RequestBody BookDto book) {
        try {
            var createdBook = bookService.createBook(book);
            return ResponseEntity.ok(new ResponseDto(true, "Book created successfully", 201, createdBook));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(false, "Unknown error occurred", 500, e.getMessage()));
        }

    }

    @GetMapping
    public ResponseEntity<ResponseDto> getBooks(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        try {
            var books = bookService.getBooks(new com.kcb.bookmaskingdemo.dto.FetchRequest(page, size));
            return ResponseEntity.ok(new ResponseDto(true, "Books fetched successfully", 200, books));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(false, "Unknown error occurred", 500, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getBookById(@PathVariable Long id) {
        try {
            var book = bookService.getBookById(id);
            if (book == null) {
                return ResponseEntity.ok(new ResponseDto(false, "Book not found", 404, null));
            }
            return ResponseEntity.ok(new ResponseDto(true, "Book fetched successfully", 200, book));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(false, "Unknown error occurred", 500, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            var updatedBook = bookService.updateBook(id, bookDto);
            if (updatedBook == null) {
                return ResponseEntity.ok(new ResponseDto(false, "Book not found", 404, null));
            }
            return ResponseEntity.ok(new ResponseDto(true, "Book updated successfully", 200, updatedBook));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(false, "Unknown error occurred", 500, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteBook(@PathVariable Long id) {
        try {
            boolean deleted = bookService.deleteBook(id);
            if (!deleted) {
                return ResponseEntity.ok(new ResponseDto(false, "Book not found", 404, null));
            }
            return ResponseEntity.ok(new ResponseDto(true, "Book deleted successfully", 200, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(false, "Unknown error occurred", 500, e.getMessage()));
        }
    }



}
