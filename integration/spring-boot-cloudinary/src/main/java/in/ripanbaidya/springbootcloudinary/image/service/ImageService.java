package in.ripanbaidya.springbootcloudinary.image.service;

import in.ripanbaidya.springbootcloudinary.api.dto.DeleteProfilePictureResponse;
import in.ripanbaidya.springbootcloudinary.api.dto.ResponseWrapper;
import in.ripanbaidya.springbootcloudinary.api.dto.UploadProfilePictureResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Service interface for managing image operations.
 * <p>This interface defines the contract for uploading and deleting profile pictures
 * using cloud storage services like Cloudinary.</p>
 */
public interface ImageService {

    /**
     * Uploads a profile picture to the cloud storage.
     *
     * @param file the multipart file containing the image to upload
     * @return a ResponseWrapper containing UploadProfilePictureResponse with upload details
     */
    ResponseWrapper<UploadProfilePictureResponse> uploadPicture(MultipartFile file);

    /**
     * Deletes a profile picture from the cloud storage.
     *
     * @param imageId the unique identifier of the image to delete
     * @return a ResponseWrapper containing DeleteProfilePictureResponse with deletion details
     */
    ResponseWrapper<DeleteProfilePictureResponse> deletePicture(UUID imageId);

}