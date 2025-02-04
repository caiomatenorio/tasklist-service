package io.github.caiomatenorio.tasklist_service.config;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecureRandomConfig {
    @Bean
    SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
