package com.kcb.bankmaskingspringbootstarter.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.kcb.bankmaskingspringbootstarter.config.MaskingProperties;
import com.kcb.bankmaskingspringbootstarter.config.enums.MaskingStyles;
import com.kcb.bankmaskingspringbootstarter.service.MaskingStrategy;
import com.kcb.bankmaskingspringbootstarter.service.imp.FullMaskingStrategy;
import com.kcb.bankmaskingspringbootstarter.service.imp.LastFourMaskingStrategy;
import com.kcb.bankmaskingspringbootstarter.service.imp.PartialMaskingStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for intercepting the log message and masking the sensitive
 * fields as per the configuration before writing on the logs
 */
public class LoggingMessageConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();

        // fetch from static holder — Logback cannot inject beans
        MaskingProperties properties = MaskingContext.getProperties();

        if (properties == null || !properties.isEnabled() || properties.getFields().isEmpty()) {
            return message;
        }

        MaskingStrategy strategy = resolveStrategy(properties);
        return processMasking(message, properties, strategy);
    }

    /**
     * This method iterates through the list of fields specified in the masking properties,
     * calling the maskField method for each field to apply the masking logic.
     * The final masked message is returned after processing all specified fields.
     *
     * @param message the log output
     * @return masked log output
     */
    private String processMasking(String message, MaskingProperties properties, MaskingStrategy strategy) {
        for (String field : properties.getFields()) {
            message = maskField(message, field, properties.getMaskCharactor(), strategy);
        }
        return message;
    }

    /**
     * This method takes in a field name and the log message, applies a regex pattern to find
     * occurrences of the field in the message, captures the raw value, and then uses the masking
     * strategy to mask it. The masked value is then replaced in the original message,
     * and the final masked message is returned.
     *
     * @param fieldName the name of the field to be masked
     * @param message incoming message
     * @return modified log message with the specified field masked
     */
    private String maskField(String message, String fieldName, String maskCharacter, MaskingStrategy strategy) {
        Pattern pattern = Pattern.compile(
                "(" + Pattern.quote(fieldName) + "=)([^,)\\s}]+)"
        );

        Matcher matcher = pattern.matcher(message);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String fieldPrefix = matcher.group(1); // e.g. "phone="
            String rawValue    = matcher.group(2); // e.g. "1234567890"

            String maskedValue = strategy.mask(rawValue, String.valueOf(maskCharacter));

            // quoteReplacement handles special characters like $ and \ in masked values
            matcher.appendReplacement(result, Matcher.quoteReplacement(fieldPrefix + maskedValue));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    private MaskingStrategy resolveStrategy(MaskingProperties properties) {
        return switch (MaskingStyles.valueOf(String.valueOf(properties.getMaskingStyle()))) {
            case FULL    -> new FullMaskingStrategy();
            case LAST4   -> new LastFourMaskingStrategy();
            case PARTIAL -> new PartialMaskingStrategy();
        };
    }
}
