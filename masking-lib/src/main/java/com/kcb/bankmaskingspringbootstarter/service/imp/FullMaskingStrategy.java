package com.kcb.bankmaskingspringbootstarter.service.imp;

import com.kcb.bankmaskingspringbootstarter.service.MaskingStrategy;

public class FullMaskingStrategy implements MaskingStrategy {
    @Override
    public String mask(String input, String maskingCharacter) {
       if (input == null || input.isEmpty()) {
           return input;
        }
        return String.valueOf(maskingCharacter).repeat(input.length());
    }
}
