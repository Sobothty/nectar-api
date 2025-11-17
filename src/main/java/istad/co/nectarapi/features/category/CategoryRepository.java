package istad.co.nectarapi.features.category;

import istad.co.nectarapi.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    boolean existsByUuid(String uuid);
    Optional<Category> findByUuid(String uuid);

    @Modifying
    @Query("UPDATE Category c SET c.isDeleted = true WHERE c.uuid = ?1")
    void deleteSoftByUuid(String uuid);
}
