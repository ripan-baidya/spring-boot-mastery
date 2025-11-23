package in.ripanbaidya.openaiimagegeneration.controller;

import in.ripanbaidya.openaiimagegeneration.dto.CustomImageRequest;
import in.ripanbaidya.openaiimagegeneration.dto.ImageRequest;
import in.ripanbaidya.openaiimagegeneration.service.ImageService;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ImageResponse> generateImage(@RequestBody ImageRequest request) {
        ImageResponse response = imageService.generateImage(request.prompt());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/generate/custom")
    public ResponseEntity<ImageResponse> generateImage(@RequestBody CustomImageRequest request) {
        ImageResponse response = imageService.generateImage(
                request.prompt(),
                request.quality(),
                request.width(),
                request.height());
        return ResponseEntity.status(200).body(response);
    }
}
