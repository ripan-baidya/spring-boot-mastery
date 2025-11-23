package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    // Make sure you are using the exact same name as in application.yaml
    // In application.yml file, you will find app.jwt.secret and app.jwt.expiration
    private String secret;

    private long expiration;


    // *************** Getters & Setters ***************

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
