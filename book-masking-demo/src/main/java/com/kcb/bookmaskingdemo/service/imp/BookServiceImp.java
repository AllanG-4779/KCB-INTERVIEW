package com.kcb.bookmaskingdemo.service.imp;

import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.dto.FetchRequest;
import com.kcb.bookmaskingdemo.mdels.Book;
import com.kcb.bookmaskingdemo.service.BookService;
import com.kcb.bookmaskingdemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookDto createBook(BookDto bookDto) {
        log.info("Incoming request with payload book with payload {} ", bookDto);
        Book book = toEntity(bookDto);
        return toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getBooks(FetchRequest request) {
        var books =  bookRepository
                .findAll(PageRequest.of(request.getPage(), request.getSize()))

                .map(this::toDto)
                .toList();
        log.info("Fetched books with payload {} ", books);
        return books;

    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        log.info("Updating book with id {} and payload {} ", id, bookDto);
        return bookRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(bookDto.getTitle());
                    existing.setAuthor(bookDto.getAuthor());
                    existing.setEmail(bookDto.getEmail());
                    existing.setPhoneNumber(bookDto.getPhoneNumber());
                    existing.setPublisher(bookDto.getPublisher());
                    return toDto(bookRepository.save(existing));
                })
                .orElse(null);
    }

    @Override
    public boolean deleteBook(Long id) {
        log.info("Deleting book with id {} ", id);
        if (!bookRepository.existsById(id)) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }

    private BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getEmail(),
                book.getPhoneNumber(),
                book.getPublisher()
        );
    }

    private Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setEmail(dto.getEmail());
        book.setPhoneNumber(dto.getPhoneNumber());
        book.setPublisher(dto.getPublisher());
        log.info("Mapped book entity with payload {} ", book);
        return book;
    }
}
