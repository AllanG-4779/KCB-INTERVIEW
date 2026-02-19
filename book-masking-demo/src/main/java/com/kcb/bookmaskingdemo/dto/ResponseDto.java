package com.kcb.bookmaskingdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private boolean successful;
    private String message;
    private int statusCode;
    private Object data;
}
