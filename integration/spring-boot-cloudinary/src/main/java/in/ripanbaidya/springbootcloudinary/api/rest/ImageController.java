package in.ripanbaidya.springbootcloudinary.api.rest;

import in.ripanbaidya.springbootcloudinary.api.dto.DeleteProfilePictureResponse;
import in.ripanbaidya.springbootcloudinary.api.dto.ResponseWrapper;
import in.ripanbaidya.springbootcloudinary.api.dto.UploadProfilePictureResponse;
import in.ripanbaidya.springbootcloudinary.image.service.ImageService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<UploadProfilePictureResponse>> uploadPicture(
            @RequestParam("file") @NotNull MultipartFile file) {

        var response = imageService.uploadPicture(file);

        var status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/{imageId}/delete-picture")
    public ResponseEntity<ResponseWrapper<DeleteProfilePictureResponse>> deletePicture(
            @PathVariable UUID imageId) {

        var response = imageService.deletePicture(imageId);

        var status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
