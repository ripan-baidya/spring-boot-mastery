package com.example.controller;

import com.example.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestJwtProperties {

    private static final Logger log = LoggerFactory.getLogger(TestJwtProperties.class);

    private final JwtProperties jwtProperties;

    @Autowired
    public TestJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @GetMapping
    public ResponseEntity<String> testJwtProperties() {
        log.info("jwt properties: {}, {}", jwtProperties.getSecret(), jwtProperties.getExpiration());

        return ResponseEntity.ok("Hello World!");
    }
}
