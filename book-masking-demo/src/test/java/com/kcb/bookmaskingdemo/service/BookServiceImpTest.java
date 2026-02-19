package com.kcb.bookmaskingdemo.service;

import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.dto.FetchRequest;
import com.kcb.bookmaskingdemo.mdels.Book;
import com.kcb.bookmaskingdemo.service.imp.BookServiceImp;
import com.kcb.bookmaskingdemo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImp bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Clean Code", "Robert Martin", "bob@example.com", "0712345678", "Prentice Hall");
        bookDto = new BookDto(null, "Clean Code", "Robert Martin", "bob@example.com", "0712345678", "Prentice Hall");
    }

    @Test
    void createBook_shouldSaveAndReturnMappedDto() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.createBook(bookDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getAuthor()).isEqualTo("Robert Martin");
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
        assertThat(result.getPhoneNumber()).isEqualTo("0712345678");
        assertThat(result.getPublisher()).isEqualTo("Prentice Hall");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getBooks_shouldReturnPagedDtoList() {
        Page<Book> page = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        List<BookDto> result = bookService.getBooks(new FetchRequest(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
    }

    @Test
    void getBooks_shouldReturnEmptyList_whenNoBooksExist() {
        Page<Book> emptyPage = new PageImpl<>(List.of());
        when(bookRepository.findAll(PageRequest.of(0, 5))).thenReturn(emptyPage);

        List<BookDto> result = bookService.getBooks(new FetchRequest(0, 5));

        assertThat(result).isEmpty();
    }

    @Test
    void getBookById_shouldReturnDto_whenFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto result = bookService.getBookById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Clean Code");
    }

    @Test
    void getBookById_shouldReturnNull_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        BookDto result = bookService.getBookById(99L);

        assertThat(result).isNull();
    }

    @Test
    void updateBook_shouldUpdateFieldsAndReturnDto() {
        BookDto updateDto = new BookDto(null, "Refactoring", "Martin Fowler", "mf@example.com", "0799999999", "Addison-Wesley");
        Book savedBook = new Book(1L, "Refactoring", "Martin Fowler", "mf@example.com", "0799999999", "Addison-Wesley");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDto result = bookService.updateBook(1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Refactoring");
        assertThat(result.getAuthor()).isEqualTo("Martin Fowler");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_shouldReturnNull_whenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        BookDto result = bookService.updateBook(99L, bookDto);

        assertThat(result).isNull();
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_shouldReturnTrue_whenBookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        boolean result = bookService.deleteBook(1L);

        assertThat(result).isTrue();
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_shouldReturnFalse_whenBookNotFound() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        boolean result = bookService.deleteBook(99L);

        assertThat(result).isFalse();
        verify(bookRepository, never()).deleteById(any());
    }
}
