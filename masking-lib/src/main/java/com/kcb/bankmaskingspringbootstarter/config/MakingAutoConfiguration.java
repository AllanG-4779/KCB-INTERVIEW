package com.kcb.kcbmaskinglib.config;


import com.kcb.bankmaskingspringbootstarter.config.MaskingProperties;
import com.kcb.bankmaskingspringbootstarter.logback.MaskingContext;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(MaskingProperties.class)
@ConditionalOnProperty(
        prefix = "pii.masking",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class MakingAutoConfiguration {

    @Bean
    public ApplicationRunner maskingContextInitializer(MaskingProperties properties) {
        return args -> MaskingContext.setProperties(properties);
    }
}
