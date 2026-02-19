package com.kcb.bankmaskingspringbootstarter.logback;


import com.kcb.bankmaskingspringbootstarter.config.MaskingProperties;

/**
 * Static holder that bridges the gap between Spring's context
 * and Logback's converter.
 *
 * Logback initializes before Spring, so the converter cannot
 * receive beans via injection. Instead, MaskingAutoConfiguration
 * populates this holder once Spring is ready, and the converter
 * reads from it on every log call.
 */
public class MaskingContext {

    private static MaskingProperties properties;

    public static void setProperties(MaskingProperties props) {
        properties = props;
    }

    public static MaskingProperties getProperties() {
        return properties;
    }
}
