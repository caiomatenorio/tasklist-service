package io.github.caiomatenorio.tasklist_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private Session session;
    private Jwt jwt;

    @Getter
    @Setter
    public static class Session {
        private int expirationSeconds;
        private int refreshTokenLength;
    }

    @Getter
    @Setter
    public static class Jwt {
        private int expirationSeconds;
        private String secret;
    }
}
