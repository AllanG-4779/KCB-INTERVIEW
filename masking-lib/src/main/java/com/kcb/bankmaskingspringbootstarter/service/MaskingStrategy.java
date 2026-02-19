package com.kcb.bankmaskingspringbootstarter.service;

public interface MaskingStrategy {
    public String mask(String input, String maskingCharacter);
}
