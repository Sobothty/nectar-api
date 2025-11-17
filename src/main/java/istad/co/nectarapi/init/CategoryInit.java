package istad.co.nectarapi.init;

import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.features.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryInit {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }

        List<Category> categories = List.of(
                createCategory("Fruits", "Fresh and organic fruits"),
                createCategory("Vegetables", "Healthy green vegetables"),
                createCategory("Beverages", "Soft drinks, juices, and bottled water"),
                createCategory("Snacks", "Chips, biscuits, and packaged snacks"),
                createCategory("Dairy", "Milk, cheese, and dairy products")
        );

        categoryRepository.saveAll(categories);
    }

    private Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setUuid(UUID.randomUUID().toString());
        category.setIsDeleted(false);
        return category;
    }
}
