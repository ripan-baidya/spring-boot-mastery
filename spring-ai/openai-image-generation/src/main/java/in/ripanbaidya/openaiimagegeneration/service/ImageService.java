package in.ripanbaidya.openaiimagegeneration.service;

import org.springframework.ai.image.ImageResponse;

/**
 * Service interface for generating images using OpenAI's image generation capabilities.
 */
public interface ImageService {

    ImageResponse generateImage(String prompt);

    ImageResponse generateImage(String prompt, String quality, Integer width, Integer height);
}
