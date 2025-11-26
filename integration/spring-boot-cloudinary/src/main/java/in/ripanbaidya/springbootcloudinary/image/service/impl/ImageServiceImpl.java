package in.ripanbaidya.springbootcloudinary.image.service.impl;

import in.ripanbaidya.springbootcloudinary.api.dto.DeleteProfilePictureResponse;
import in.ripanbaidya.springbootcloudinary.api.dto.ResponseWrapper;
import in.ripanbaidya.springbootcloudinary.api.dto.UploadProfilePictureResponse;
import in.ripanbaidya.springbootcloudinary.cloudinary.dto.CloudinaryUploadResult;
import in.ripanbaidya.springbootcloudinary.config.properties.CloudinaryProperties;
import in.ripanbaidya.springbootcloudinary.image.entity.Image;
import in.ripanbaidya.springbootcloudinary.cloudinary.service.CloudinaryService;
import in.ripanbaidya.springbootcloudinary.image.repository.ImageRepository;
import in.ripanbaidya.springbootcloudinary.image.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;
    private final ImageRepository imageRepository;
    @Autowired
    public ImageServiceImpl(CloudinaryService cloudinaryService, CloudinaryProperties cloudinaryProperties, 
                            ImageRepository imageRepository) {
        this.cloudinaryService = cloudinaryService;
        this.cloudinaryProperties = cloudinaryProperties;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public ResponseWrapper<UploadProfilePictureResponse> uploadPicture(MultipartFile file) {
        // Upload new image
        CloudinaryUploadResult uploadResult = cloudinaryService
                .uploadImage(file, cloudinaryProperties.upload().folder());

        // Update Image entity
        Image image = new Image();
        image.setImageUrl(uploadResult.url());
        image.setPublicId(uploadResult.publicId());
        
        Image savedImage = imageRepository.save(image);
        log.info("Image uploaded successfully. Public ID: {}, URL: {}", uploadResult.publicId(), uploadResult.url());
        
        var response = new UploadProfilePictureResponse(
                savedImage.getId(),
                true,
                savedImage.getImageUrl(),
                savedImage.getPublicId(),
                LocalDateTime.now()
        );
        return ResponseWrapper.success(response, "Image uploaded successfully");
    }

    @Override
    public ResponseWrapper<DeleteProfilePictureResponse> deletePicture(UUID imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new EntityNotFoundException("Image not found with ID: " + imageId)
        );
        cloudinaryService.deleteImage(image.getPublicId());
        imageRepository.delete(image);

        var response = new DeleteProfilePictureResponse(
                imageId,
                true,
                LocalDateTime.now()
        );
        return ResponseWrapper.success(response, "Image deleted successfully");
    }
}
