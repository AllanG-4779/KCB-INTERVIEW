package com.kcb.bookmaskingdemo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String email;
    private String phoneNumber;
    private String publisher;
}
