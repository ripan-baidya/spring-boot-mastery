package in.ripanbaidya.springbootcloudinary.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteProfilePictureResponse(

        UUID id,

        boolean deleted,

        LocalDateTime deletedAt
) {}
