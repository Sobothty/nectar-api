package istad.co.nectarapi.features.product;

import istad.co.nectarapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByUuid(String uuid);
    boolean existsByName(String name);
    boolean existsByUuid(String uuid);

    @Modifying
    @Query("UPDATE Product p SET p.isDeleted= true WHERE p.uuid = ?1")
    void deleteSoftByUuid(String uuid);
}
