package com.kcb.bankmaskingspringbootstarter.service.imp;

import com.kcb.bankmaskingspringbootstarter.service.MaskingStrategy;

public class PartialMaskingStrategy implements MaskingStrategy {
    private final int UNMASKED_CHARACTERS = 2;

    @Override
    public String mask(String input, String maskingCharacter) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        int length = input.length();
        // Too few characters, mask everything
        if (length <= (UNMASKED_CHARACTERS * 2)) {
            return String.valueOf(maskingCharacter).repeat(length);
        }
        String unmaskedPart = input.substring(0, UNMASKED_CHARACTERS);
        String maskedPart = String.valueOf(maskingCharacter).repeat(length - (UNMASKED_CHARACTERS * 2));
        String unMaskedEndPart = input.substring(length - UNMASKED_CHARACTERS);
        return unmaskedPart + maskedPart + unMaskedEndPart;
    }
}
