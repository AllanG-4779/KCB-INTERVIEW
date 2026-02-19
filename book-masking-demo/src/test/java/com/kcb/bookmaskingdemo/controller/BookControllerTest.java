package com.kcb.bookmaskingdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.dto.FetchRequest;
import com.kcb.bookmaskingdemo.service.BookService;
import com.kcb.bookmaskingdemo.service.imp.BookServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookServiceImp bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    private final BookDto sampleBook =
            new BookDto(1L, "Clean Code", "Robert Martin", "bob@example.com", "0712345678", "Prentice Hall");


    @Test
    void createBook_shouldReturnSuccessResponse() throws Exception {
        when(bookService.createBook(any(BookDto.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("Book created successfully"))
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }

    @Test
    void createBook_shouldReturnErrorResponse_onException() throws Exception {
        when(bookService.createBook(any(BookDto.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(500));
    }


    @Test
    void getBooks_shouldReturnListOfBooks() throws Exception {
        when(bookService.getBooks(any(FetchRequest.class))).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Clean Code"));
    }

    @Test
    void getBooks_shouldReturnEmptyList_whenNoBooks() throws Exception {
        when(bookService.getBooks(any(FetchRequest.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getBooks_shouldReturnErrorResponse_onException() throws Exception {
        when(bookService.getBooks(any(FetchRequest.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    // ─── getBookById ──────────────────────────────────────────────────────────

    @Test
    void getBookById_shouldReturnBook_whenFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }

    @Test
    void getBookById_shouldReturnNotFound_whenBookMissing() throws Exception {
        when(bookService.getBookById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/books/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void getBookById_shouldReturnErrorResponse_onException() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(500));
    }

    // ─── updateBook ───────────────────────────────────────────────────────────

    @Test
    void updateBook_shouldReturnUpdatedBook_whenFound() throws Exception {
        BookDto updated = new BookDto(1L, "Refactoring", "Martin Fowler", "mf@example.com", "0799999999", "Addison-Wesley");
        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                .andExpect(jsonPath("$.data.title").value("Refactoring"));
    }

    @Test
    void updateBook_shouldReturnNotFound_whenBookMissing() throws Exception {
        when(bookService.updateBook(eq(99L), any(BookDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void updateBook_shouldReturnErrorResponse_onException() throws Exception {
        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(500));
    }


    @Test
    void deleteBook_shouldReturnSuccess_whenDeleted() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Book deleted successfully"));
    }

    @Test
    void deleteBook_shouldReturnNotFound_whenBookMissing() throws Exception {
        when(bookService.deleteBook(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/books/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void deleteBook_shouldReturnErrorResponse_onException() throws Exception {
        when(bookService.deleteBook(1L)).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(500));
    }
}
