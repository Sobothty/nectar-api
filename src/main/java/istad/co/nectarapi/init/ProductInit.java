package istad.co.nectarapi.init;

import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.domain.Product;
import istad.co.nectarapi.features.category.CategoryRepository;
import istad.co.nectarapi.features.product.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductInit {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initProducts() {
        if (productRepository.count() > 0) {
            return;
        }

        // Get all categories from database
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.warn("No categories found! Please run CategoryInit first.");
            return;
        }

        List<Product> products = new ArrayList<>();

        // Loop through categories and add sample products for each
        for (Category category : categories) {
            switch (category.getName()) {
                case "Fruits" -> products.addAll(createFruits(category));
                case "Vegetables" -> products.addAll(createVegetables(category));
                case "Beverages" -> products.addAll(createBeverages(category));
                case "Snacks" -> products.addAll(createSnacks(category));
                case "Dairy" -> products.addAll(createDairy(category));
            }
        }

        productRepository.saveAll(products);
        log.info("Initialized {} products successfully!", products.size());
    }

    private List<Product> createFruits(Category category) {
        return List.of(
                createProduct("Apple", "Fresh red apples from New Zealand.", new BigDecimal("1.50"), category),
                createProduct("Banana", "Sweet ripe bananas full of potassium.", new BigDecimal("0.75"), category),
                createProduct("Orange", "Juicy and full of vitamin C.", new BigDecimal("1.20"), category),
                createProduct("Grapes", "Fresh green seedless grapes.", new BigDecimal("2.80"), category)
        );
    }

    private List<Product> createVegetables(Category category) {
        return List.of(
                createProduct("Carrot", "Crunchy and fresh carrots.", new BigDecimal("0.90"), category),
                createProduct("Broccoli", "Organic broccoli rich in fiber.", new BigDecimal("1.60"), category),
                createProduct("Tomato", "Fresh tomatoes for salad or cooking.", new BigDecimal("1.10"), category),
                createProduct("Lettuce", "Crispy green lettuce leaves.", new BigDecimal("0.95"), category)
        );
    }

    private List<Product> createBeverages(Category category) {
        return List.of(
                createProduct("Coca-Cola", "Refreshing soft drink.", new BigDecimal("1.25"), category),
                createProduct("Orange Juice", "100% natural fruit juice.", new BigDecimal("2.00"), category),
                createProduct("Green Tea", "Healthy unsweetened green tea.", new BigDecimal("1.50"), category),
                createProduct("Mineral Water", "Pure natural mineral water.", new BigDecimal("0.80"), category)
        );
    }

    private List<Product> createSnacks(Category category) {
        return List.of(
                createProduct("Potato Chips", "Crispy salted chips.", new BigDecimal("1.20"), category),
                createProduct("Chocolate Bar", "Delicious milk chocolate.", new BigDecimal("1.50"), category),
                createProduct("Biscuits", "Crunchy butter biscuits.", new BigDecimal("1.10"), category),
                createProduct("Popcorn", "Tasty caramel popcorn.", new BigDecimal("1.40"), category)
        );
    }

    private List<Product> createDairy(Category category) {
        return List.of(
                createProduct("Fresh Milk", "High-quality full cream milk.", new BigDecimal("1.90"), category),
                createProduct("Yogurt", "Delicious fruit-flavored yogurt.", new BigDecimal("1.30"), category),
                createProduct("Cheese", "Premium cheddar cheese.", new BigDecimal("3.50"), category),
                createProduct("Butter", "Creamy natural butter.", new BigDecimal("2.40"), category)
        );
    }


    private Product createProduct(String name, String description, BigDecimal price, Category category) {
        Product product = new Product();
        product.setUuid(UUID.randomUUID().toString());
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setImageUrl("https://placehold.co/600x400?text=" + name.replace(" ", "+"));
        return product;
    }
}
