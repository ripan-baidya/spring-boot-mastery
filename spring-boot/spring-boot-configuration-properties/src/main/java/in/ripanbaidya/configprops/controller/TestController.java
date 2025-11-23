package in.ripanbaidya.configprops.controller;

import in.ripanbaidya.configprops.properties.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample controller class for demonstration purposes.
 * This class demonstrates how to use {@code JwtProperties} to access JWT-related properties
 * and explores alternative approaches for accessing properties without dedicated property classes.
 *
 * @author Ripan Baidya
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    /**
     * We can use {@code @Value} annotation to access properties without dedicated property classes.
     * we can also define default values for properties, which will be used if the property is not defined.
     */
    @Value("${jwt.secret:your-secret-key-here}")
    private String secret;

    private final JwtProperties jwtProperties;

    @Autowired
    public TestController(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @GetMapping
    public ResponseEntity<String> test() {
        // Accessing properties in our application
        log.info("Jwt properties: secret: {}, expiration: {}, header: {}",
                jwtProperties.secret(), jwtProperties.expiration(), jwtProperties.header());

        return ResponseEntity.ok().body("Hello World!");
    }
}
