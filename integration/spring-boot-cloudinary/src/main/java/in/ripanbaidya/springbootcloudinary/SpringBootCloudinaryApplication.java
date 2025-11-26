package in.ripanbaidya.springbootcloudinary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "in.ripanbaidya.springbootcloudinary.config.properties")
public class SpringBootCloudinaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCloudinaryApplication.class, args);
    }

}
