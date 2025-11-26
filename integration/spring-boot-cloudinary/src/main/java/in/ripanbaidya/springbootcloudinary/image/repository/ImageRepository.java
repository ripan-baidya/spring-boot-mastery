package in.ripanbaidya.springbootcloudinary.image.repository;

import in.ripanbaidya.springbootcloudinary.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    Optional<Image> findByImageUrl(String imageUrl);

    Optional<Image> findByPublicId(String publicId);
}
