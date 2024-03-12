package com.bookingcar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "db")
public record DatabaseProperties(String url,
                                 String username,
                                 String password) {
}
