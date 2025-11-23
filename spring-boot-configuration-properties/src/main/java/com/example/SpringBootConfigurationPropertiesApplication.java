package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
// Scan the package and load property classes mark with @ConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.example.config")
public class SpringBootConfigurationPropertiesApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootConfigurationPropertiesApplication.class, args);
	}

}
