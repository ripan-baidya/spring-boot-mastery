package in.ripanbaidya.openaiimagegeneration.service.impl;

import in.ripanbaidya.openaiimagegeneration.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final OpenAiImageModel openAiImageModel;

    @Autowired
    public ImageServiceImpl(OpenAiImageModel openAiImageModel) {
        this.openAiImageModel = openAiImageModel;
    }

    @Override
    public ImageResponse generateImage(String prompt) {
        log.info("Generating image for prompt: {}", prompt);
        return openAiImageModel.call(
                new ImagePrompt(prompt)
        );
    }

    @Override
    public ImageResponse generateImage(String prompt, String quality, Integer width, Integer height) {
        log.info("Generating image for prompt: {}, quality: {}, width: {}, height: {}", prompt, quality, width, height);
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .quality(quality)
                .width(width)
                .height(height)
                .build();

        return openAiImageModel.call(
                new ImagePrompt(prompt, options)
        );
    }
}
