package in.ripanbaidya.openaiimagegeneration.dto;

public record CustomImageRequest(

        String prompt,

        String quality,

        Integer width,

        Integer height
) {

    public CustomImageRequest {
        if (quality == null) {
            quality = "standard";
        }
        if (width == null) {
            width = 1024;
        }
        if (height == null) {
            height = 1024;
        }
    }
}
