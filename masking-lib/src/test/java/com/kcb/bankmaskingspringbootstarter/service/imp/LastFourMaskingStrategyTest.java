package com.kcb.bankmaskingspringbootstarter.service.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LastFourMaskingStrategyTest {

    private LastFourMaskingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LastFourMaskingStrategy();
    }

    @Test
    void mask_shouldReturnNull_whenInputIsNull() {
        assertThat(strategy.mask(null, "*")).isNull();
    }

    @Test
    void mask_shouldReturnEmpty_whenInputIsEmpty() {
        assertThat(strategy.mask("", "*")).isEmpty();
    }

    @Test
    void mask_shouldMaskAll_whenLengthExactlyFour() {
        assertThat(strategy.mask("1234", "*")).isEqualTo("****");
    }

    @Test
    void mask_shouldMaskAll_whenLengthLessThanFour() {
        assertThat(strategy.mask("123", "*")).isEqualTo("***");
    }

    @Test
    void mask_shouldExposeLastFour_whenLengthGreaterThanFour() {
        assertThat(strategy.mask("1234567890", "*")).isEqualTo("******7890");
    }

    @Test
    void mask_shouldExposeLastFour_withExactlyFivChars() {
        assertThat(strategy.mask("12345", "*")).isEqualTo("*2345");
    }
}