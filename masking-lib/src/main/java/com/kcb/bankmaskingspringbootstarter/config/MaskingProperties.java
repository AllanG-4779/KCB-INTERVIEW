package com.kcb.bankmaskingspringbootstarter.config;

import com.kcb.bankmaskingspringbootstarter.config.enums.MaskingStyles;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "pii.masking")
public class MaskingProperties {
    private boolean enabled;
    private List<String> fields;
    private MaskingStyles maskingStyle;
    private String maskCharactor;

}
