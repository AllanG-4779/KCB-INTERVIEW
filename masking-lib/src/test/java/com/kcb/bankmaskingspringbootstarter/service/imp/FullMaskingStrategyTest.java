package com.kcb.bankmaskingspringbootstarter.service.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FullMaskingStrategyTest {

    private FullMaskingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FullMaskingStrategy();
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
    void mask_shouldMaskEntireString() {
        assertThat(strategy.mask("hello", "*")).isEqualTo("*****");
    }

    @Test
    void mask_shouldRespectMaskCharacter() {
        assertThat(strategy.mask("abc", "#")).isEqualTo("###");
    }

    @Test
    void mask_shouldMaskSingleCharacter() {
        assertThat(strategy.mask("x", "*")).isEqualTo("*");
    }
}