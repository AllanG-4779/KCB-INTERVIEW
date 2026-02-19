package com.kcb.bankmaskingspringbootstarter.service.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PartialMaskingStrategyTest {

    private PartialMaskingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PartialMaskingStrategy();
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
        // <= UNMASKED_CHARACTERS * 2 (4), mask everything
        assertThat(strategy.mask("abcd", "*")).isEqualTo("****");
    }

    @Test
    void mask_shouldMaskAll_whenLengthLessThanFour() {
        assertThat(strategy.mask("ab", "*")).isEqualTo("**");
    }

    @Test
    void mask_shouldExposeFirstAndLastTwo_whenLongEnough() {
        // "hello world" -> "he" + "*******" + "ld"
        assertThat(strategy.mask("hello world", "*")).isEqualTo("he*******ld");
    }

    @Test
    void mask_shouldRespectMaskCharacter() {
        assertThat(strategy.mask("abcdefgh", "#")).isEqualTo("ab####gh");
    }

    @Test
    void mask_shouldWorkWithExactlyFiveChars() {
        // 5 chars: expose first 2, mask 1, expose last 2
        assertThat(strategy.mask("abcde", "*")).isEqualTo("ab*de");
    }
}