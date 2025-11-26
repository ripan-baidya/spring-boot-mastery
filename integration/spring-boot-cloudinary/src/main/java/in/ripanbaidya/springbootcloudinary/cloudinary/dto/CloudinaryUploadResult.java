package in.ripanbaidya.springbootcloudinary.cloudinary.dto;

/**
 * Data Transfer Object representing the result of a file upload to Cloudinary.
 * <p>
 * This record encapsulates all relevant information returned by Cloudinary after
 * a successful file upload operation, including URLs, metadata, and resource details.
 * </p>
 */
public record CloudinaryUploadResult(

        String publicId,     // The unique public identifier for the uploaded resource in Cloudinary

        String url           // The HTTP URL to access the uploaded resource

) {}
