package com.kcb.bankmaskingspringbootstarter.service.imp;

import com.kcb.bankmaskingspringbootstarter.service.MaskingStrategy;

public class LastFourMaskingStrategy implements MaskingStrategy {
    @Override
    public String mask(String input, String maskingCharacter) {
         if (input == null || input.isEmpty()) {
            return input;
         }
        int length = input.length();
         if (length <= 4) {
             return String.valueOf(maskingCharacter).repeat(length);
         }
        String maskedPart = String.valueOf(maskingCharacter).repeat(length - 4);
        String unmaskedPart = input.substring(length - 4);
        return maskedPart + unmaskedPart;

    }
}
