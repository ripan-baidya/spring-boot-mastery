package in.ripanbaidya.springbootcloudinary.cloudinary.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import in.ripanbaidya.springbootcloudinary.config.properties.CloudinaryProperties;
import in.ripanbaidya.springbootcloudinary.cloudinary.dto.CloudinaryUploadResult;
import in.ripanbaidya.springbootcloudinary.cloudinary.service.CloudinaryService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Implementation of the CloudinaryService interface for handling image operations with Cloudinary.
 */
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinary;
    private final CloudinaryProperties cloudinaryProperties;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary, CloudinaryProperties cloudinaryProperties) {
        this.cloudinary = cloudinary;
        this.cloudinaryProperties = cloudinaryProperties;
    }

    // Uploads an image to Cloudinary.
    @Override
    public CloudinaryUploadResult uploadImage(MultipartFile file, String folder) {
        String targetFolder = (folder != null && !folder.trim().isEmpty()) ? folder : cloudinaryProperties.upload().folder();
        log.info("Initiating image upload. Filename: {}, Target Folder: {}", file.getOriginalFilename(), targetFolder);

        // Validate file
        validateFile(file);

        try {
            // Prepare upload parameters
            Map<String, Object> uploadParams = buildUploadParams(targetFolder);

            // Upload to Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            // Parse and return result
            CloudinaryUploadResult result = mapToUploadResult(uploadResult);

            log.info("Image uploaded successfully. Public ID: {}", result.publicId());

            return result;

        } catch (IOException e) {
            log.error("IO Exception occurred while uploading image: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Network or IO error occurred while uploading image");
        } catch (Exception e) {
            log.error("Unexpected error during Cloudinary upload for file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Unexpected error occurred while uploading image");
        }
    }

    /**
     * Deletes an image from Cloudinary using its public ID.
     */
    @Override
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.trim().isEmpty()) {
            log.warn("Delete operation skipped: Public ID is null or empty.");
            return;
        }

        log.info("Attempting to delete image with Public ID: {}", publicId);

        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
            String deleteResult = (String) result.get("result");

            if ("ok".equals(deleteResult)) {
                log.info("Successfully deleted image. Public ID: {}", publicId);
            } else if ("not found".equals(deleteResult)) {
                log.warn("Image not found for deletion. Public ID: {}", publicId);
            } else {
                log.warn("Unexpected delete result: {}. Public ID: {}", deleteResult, publicId);
                // Consider if you want to throw exception here or just log warning. 
                // For now logging warning as per previous logic but clearer.
            }

        } catch (IOException e) {
            log.error("IO Exception during image deletion. Public ID: {}", publicId, e);
            throw new RuntimeException("Network or IO error occurred during deletion");
        } catch (Exception e) {
            log.error("Unexpected error during image deletion. Public ID: {}", publicId, e);
            throw new RuntimeException("Unexpected error occurred during deletion");
        }
    }

    /**
     * Generates a transformed URL for an image.
     */
    @Override
    public String generateTransformedUrl(String publicId, Integer width, Integer height, String crop) {
        if (publicId == null || publicId.trim().isEmpty()) {
            return null;
        }

        try {
            return cloudinary.url()
                    .secure(true)
                    .transformation(new Transformation()
                            .width(width)
                            .height(height)
                            .crop(crop))
                    .generate(publicId);
        } catch (Exception e) {
            log.error("Error generating transformed URL for Public ID: {}", publicId, e);
            return null;
        }
    }

    /**
     * Validates the uploaded file against defined constraints.
     */
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file == null || file.isEmpty()) {
            log.error("Validation failed: File is null or empty.");
            throw new IllegalArgumentException("File is empty");
        }

        // Check file size
        long maxSize = cloudinaryProperties.upload().maxFileSize().toBytes();
        if (file.getSize() > maxSize) {
            log.error("Validation failed: File size {} bytes exceeds limit {} bytes", file.getSize(), maxSize);
            throw new IllegalArgumentException(String.format("File size exceeds limit of %d bytes", maxSize));
        }

        // Check file format
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            log.error("Validation failed: Original filename is missing.");
            throw new IllegalArgumentException("Original filename is missing");
        }

        String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        List<String> allowedFormats = cloudinaryProperties.upload().allowedFormats();

        if (!allowedFormats.contains(extension)) {
            log.error("Validation failed: Extension '{}' not allowed. Allowed: {}", extension, allowedFormats);
            throw new IllegalArgumentException(">" + extension + "< is not allowed. Allowed formats: " + allowedFormats);
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Validation failed: Invalid content type '{}'. Must start with 'image/'", contentType);
            throw new IllegalArgumentException("Invalid content type. Must start with 'image/'");
        }
    }

    /**
     * Builds upload parameters map for Cloudinary API.
     */
    private Map<String, Object> buildUploadParams(String folder) {
        CloudinaryProperties.Upload.Transformation transform =
                cloudinaryProperties.upload().transformation();

        Map<String, Object> params = new HashMap<>();

        // Basic params
        params.put("folder", folder);
        params.put("resource_type", "image");
        params.put("overwrite", false);
        params.put("unique_filename", true);

        // Transformation params for incoming transformation (preprocessing)
        Transformation transformation = new Transformation();
        boolean hasTransformation = false;

        if (transform.width() > 0) {
            transformation.width(transform.width());
            hasTransformation = true;
        }
        if (transform.height() > 0) {
            transformation.height(transform.height());
            hasTransformation = true;
        }
        if (transform.crop() != null && !transform.crop().isEmpty()) {
            transformation.crop(transform.crop());
            hasTransformation = true;
        }
        if (transform.quality() != null && !transform.quality().isEmpty()) {
            transformation.quality(transform.quality());
            hasTransformation = true;
        }
        if (transform.format() != null && !transform.format().isEmpty()) {
            transformation.fetchFormat(transform.format());
            hasTransformation = true;
        }

        if (hasTransformation) {
            params.put("transformation", transformation);
        }

        // Security and metadata
        params.put("invalidate", true);
        // Adding default tags, could be configurable
        params.put("tags", Arrays.asList("user-upload"));

        return params;
    }

    /**
     * Maps the raw upload result Map to CloudinaryUploadResult record.
     */
    private CloudinaryUploadResult mapToUploadResult(Map<String, Object> uploadResult) {
        return new CloudinaryUploadResult(
                (String) uploadResult.get("public_id"),
                (String) uploadResult.get("url")
        );
    }
}
