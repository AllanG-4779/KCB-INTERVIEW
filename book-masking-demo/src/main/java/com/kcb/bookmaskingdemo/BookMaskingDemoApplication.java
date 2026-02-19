package com.kcb.bookmaskingdemo;

import com.kcb.bookmaskingdemo.dto.BookDto;
import com.kcb.bookmaskingdemo.mdels.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookMaskingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMaskingDemoApplication.class, args);
        var test = new Book();
        test.setEmail("allang4779@gmail.com");
        test.setPhoneNumber("+254701234567");
        log.info("Let's write this book {} ", test);
    }

}
