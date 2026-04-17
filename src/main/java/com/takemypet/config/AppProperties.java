package com.takemypet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Security security = new Security();
    private final Image image = new Image();

    @Getter
    @Setter
    public static class Security {
        private int maxFailedLoginAttempts = 10;
    }

    @Getter
    @Setter
    public static class Image {
        private String baseUrl = "http://localhost:8080/images/";
        private String storageDir = "uploads/images/";
    }
}
