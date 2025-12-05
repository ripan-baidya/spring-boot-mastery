package in.ripanbaidya.jwtauth.repository;

import in.ripanbaidya.jwtauth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
