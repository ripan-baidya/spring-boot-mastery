package in.ripanbaidya.springbootcloudinary.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.List;

/**
 * Configuration properties for cloudinary.
 */
@ConfigurationProperties(prefix = "cloudinary")
public record CloudinaryProperties(

        @NotBlank(message = "Cloudinary cloud name is required") String cloudName,

        @NotBlank(message = "Cloudinary API key is required") String apiKey,

        @NotBlank(message = "Cloudinary API secret is required") String apiSecret,

        boolean secure,

        Upload upload
) {

    public record Upload(
            String folder,

            @Positive DataSize maxFileSize,

            List<String> allowedFormats,

            Transformation transformation
    ) {
        public record Transformation(
                int width,
                int height,
                String crop,
                String quality,
                String format
        ) { }
    }
}
