package in.ripanbaidya.springbootcloudinary.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.ripanbaidya.springbootcloudinary.config.properties.CloudinaryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Cloudinary cloud storage service integration.
 *
 * <p>This configuration class initializes and manages the Cloudinary service setup
 * for the application. It binds Cloudinary-related properties from the application
 * configuration and prepares the environment for cloud-based media storage operations.</p>
 *
 * @see CloudinaryProperties
 */
@Configuration
public class CloudinaryConfig {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryConfig.class);

    private static final String CLOUD_NAME = "cloud_name";
    private static final String API_KEY = "api_key";
    private static final String API_SECRET = "api_secret";
    private static final String SECURE = "secure";

    private final CloudinaryProperties cloudinaryProperties;

    @Autowired
    public CloudinaryConfig(CloudinaryProperties cloudinaryProperties) {
        this.cloudinaryProperties = cloudinaryProperties;
    }

    @Bean
    public Cloudinary cloudinary() {
        log.info("Cloudinary initialized successfully for cloud: {}", cloudinaryProperties.cloudName());
        return new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME, cloudinaryProperties.cloudName(),
                API_KEY, cloudinaryProperties.apiKey(),
                API_SECRET, cloudinaryProperties.apiSecret(),
                SECURE, cloudinaryProperties.secure()
        ));
    }
}
