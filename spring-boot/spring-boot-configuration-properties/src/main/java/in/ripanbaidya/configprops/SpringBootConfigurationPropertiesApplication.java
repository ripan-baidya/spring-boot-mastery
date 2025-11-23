package in.ripanbaidya.configprops;

import in.ripanbaidya.configprops.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
// When we use @ConfigurationProperties on a property class, we should use @ConfigurationPropertiesScan annotation in the
// Main application, It tells to scan all properties classes. inside the mentioned package.
// Or we can mention classes to scan.
@ConfigurationPropertiesScan(
        // basePackageClasses = {JwtProperties.class},
        basePackages = "in.ripanbaidya.configprops.properties"
)
public class SpringBootConfigurationPropertiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConfigurationPropertiesApplication.class, args);
    }

}
