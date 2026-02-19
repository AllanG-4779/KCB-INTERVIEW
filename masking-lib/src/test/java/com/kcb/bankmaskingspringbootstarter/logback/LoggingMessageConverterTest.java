package com.kcb.bankmaskingspringbootstarter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.kcb.bankmaskingspringbootstarter.config.MaskingProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingMessageConverterTest {

    private LoggingMessageConverter converter;

    @Mock
    private ILoggingEvent event;

    @BeforeEach
    void setUp() {
        converter = new LoggingMessageConverter();
    }

    @AfterEach
    void tearDown() {
        MaskingContext.setProperties(null);
    }

    @Test
    void convert_shouldReturnOriginalMessage_whenPropertiesNotSet() {
        when(event.getFormattedMessage()).thenReturn("email=test@example.com");

        assertThat(converter.convert(event)).isEqualTo("email=test@example.com");
    }

    @Test
    void convert_shouldReturnOriginalMessage_whenMaskingDisabled() {
        MaskingContext.setProperties(new MaskingProperties(false, List.of("email"), "FULL", "*"));
        when(event.getFormattedMessage()).thenReturn("email=test@example.com");

        assertThat(converter.convert(event)).isEqualTo("email=test@example.com");
    }

    @Test
    void convert_shouldReturnOriginalMessage_whenFieldsListIsEmpty() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of(), "FULL", "*"));
        when(event.getFormattedMessage()).thenReturn("email=test@example.com");

        assertThat(converter.convert(event)).isEqualTo("email=test@example.com");
    }

    @Test
    void convert_shouldMaskField_usingFullStrategy() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of("email"), "FULL", "*"));
        when(event.getFormattedMessage()).thenReturn("email=test@example.com");

        String result = converter.convert(event);

        assertThat(result).startsWith("email=");
        assertThat(result).doesNotContain("test@example.com");
        assertThat(result).matches("email=\\*+");
    }

    @Test
    void convert_shouldMaskField_usingPartialStrategy() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of("phone"), "PARTIAL", "*"));
        when(event.getFormattedMessage()).thenReturn("phone=0712345678");

        String result = converter.convert(event);

        assertThat(result).startsWith("phone=07");
        assertThat(result).endsWith("78");
        assertThat(result).contains("*");
    }

    @Test
    void convert_shouldMaskField_usingLast4Strategy() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of("card"), "LAST4", "*"));
        when(event.getFormattedMessage()).thenReturn("card=1234567890123456");

        String result = converter.convert(event);

        assertThat(result).endsWith("3456");
        assertThat(result).doesNotContain("123456789012");
    }

    @Test
    void convert_shouldMaskMultipleFields() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of("email", "phone"), "FULL", "*"));
        when(event.getFormattedMessage()).thenReturn("email=bob@mail.com, phone=0712345678");

        String result = converter.convert(event);

        assertThat(result).doesNotContain("bob@mail.com");
        assertThat(result).doesNotContain("0712345678");
    }

    @Test
    void convert_shouldNotAlterMessage_whenFieldNotPresent() {
        MaskingContext.setProperties(new MaskingProperties(true, List.of("ssn"), "FULL", "*"));
        when(event.getFormattedMessage()).thenReturn("email=bob@mail.com");

        String result = converter.convert(event);

        assertThat(result).isEqualTo("email=bob@mail.com");
    }
}