package in.ripanbaidya.jwtauth;

import in.ripanbaidya.jwtauth.entity.Product;
import in.ripanbaidya.jwtauth.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
public class JwtAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApplication.class, args);
    }

    @Bean
    public CommandLineRunner insertProductData(ProductRepository productRepository) {
        return args -> {
            Product p1 = new Product();
            p1.setName("Laptop");
            p1.setDescription("High-performance laptop");
            p1.setPrice(new BigDecimal("999.99"));
            p1.setQuantity(10);
            p1.setCategory("Electronics");

            Product p2 = new Product();
            p2.setName("Smartphone");
            p2.setDescription("Latest smartphone model");
            p2.setPrice(new BigDecimal("699.99"));
            p2.setQuantity(25);
            p2.setCategory("Electronics");

            Product p3 = new Product();
            p3.setName("Headphones");
            p3.setDescription("Noise-cancelling headphones");
            p3.setPrice(new BigDecimal("199.99"));
            p3.setQuantity(50);
            p3.setCategory("Accessories");

            // save all data
            productRepository.saveAll(Arrays.asList(p1, p2, p3));
        };
    }
}
