package in.ripanbaidya.springbootcloudinary.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UploadProfilePictureResponse(

        UUID id,

        boolean uploaded,

        String imageUrl,

        String publicId,

        LocalDateTime uploadedAt
) {}