package in.ripanbaidya.springbootcloudinary.cloudinary.service;

import in.ripanbaidya.springbootcloudinary.cloudinary.dto.CloudinaryUploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing image operations with Cloudinary.
 * <p>
 * This service provides methods for uploading, deleting, and transforming images
 * stored in Cloudinary cloud storage.
 * </p>
 */
public interface CloudinaryService {

    /**
     * Uploads an image file to Cloudinary.
     *
     * @param file   the multipart file to upload
     * @param folder the target folder path in Cloudinary where the image will be stored
     * @return CloudinaryUploadResult containing upload details including URLs, dimensions, and metadata
     */
    CloudinaryUploadResult uploadImage(MultipartFile file, String folder);

    /**
     * Deletes an image from Cloudinary using its public ID.
     *
     * @param publicId the unique public identifier of the image to be deleted
     */
    void deleteImage(String publicId);

    /**
     * Generates a transformed URL for an image with specified dimensions and cropping options.
     *
     * @param publicId the unique public identifier of the image
     * @param width    the desired width in pixels (can be null for no width constraint)
     * @param height   the desired height in pixels (can be null for no height constraint)
     * @param crop     the cropping mode (e.g., "fill", "fit", "scale", "crop")
     * @return the transformed image URL with applied transformations
     */
    String generateTransformedUrl(String publicId, Integer width, Integer height, String crop);
}