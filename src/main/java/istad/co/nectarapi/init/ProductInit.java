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
                createProduct("Apple", "Fresh red apples from New Zealand.", new BigDecimal("0.10"), category, "https://img.freepik.com/free-psd/close-up-delicious-apple_23-2151868338.jpg?semt=ais_hybrid&w=740&q=80"),
                createProduct("Banana", "Sweet ripe bananas full of potassium.", new BigDecimal("0.75"), category, "https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2018/08/bananas-1354785_1920-1024x683.jpg"),
                createProduct("Orange", "Juicy and full of vitamin C.", new BigDecimal("1.20"), category, "https://www.health.com/thmb/OZgW2YQtFb9qJ3PbySNei3YdgPw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Health-Stocksy_txp5e95690asrw300_Medium_934585-e870449543284eed8aa4be52fc09a4ed.jpg"),
                createProduct("Grapes", "Fresh green seedless grapes.", new BigDecimal("2.80"), category, "https://snaped.fns.usda.gov/sites/default/files/styles/crop_ratio_7_5/public/seasonal-produce/2018-05/grapes_0.jpg.webp?itok=ZiqbgHzZ")
        );
    }

    private List<Product> createVegetables(Category category) {
        return List.of(
                createProduct("Carrot", "Crunchy and fresh carrots.", new BigDecimal("0.90"), category, "https://static01.nyt.com/images/2014/05/22/dining/Carrots-With-Orange-And-Cardamom/Carrots-With-Orange-And-Cardamom-jumbo.jpg"),
                createProduct("Broccoli", "Organic broccoli rich in fiber.", new BigDecimal("1.60"), category, "https://snaped.fns.usda.gov/sites/default/files/seasonal-produce/2018-05/broccoli.jpg"),
                createProduct("Tomato", "Fresh tomatoes for salad or cooking.", new BigDecimal("1.10"), category, "https://media.post.rvohealth.io/wp-content/uploads/2020/09/AN313-Tomatoes-732x549-Thumb.jpg"),
                createProduct("Lettuce", "Crispy green lettuce leaves.", new BigDecimal("0.95"), category, "https://media.newyorker.com/photos/5b6b08d3a676470b4ea9b91f/1:1/w_1748,h_1748,c_limit/Rosner-Lettuce.jpg")
        );
    }

    private List<Product> createBeverages(Category category) {
        return List.of(
                createProduct("Coca-Cola", "Refreshing soft drink.", new BigDecimal("1.25"), category, "https://upload.wikimedia.org/wikipedia/commons/2/27/Coca_Cola_Flasche_-_Original_Taste.jpg"),
                createProduct("Orange Juice", "100% natural fruit juice.", new BigDecimal("2.00"), category, "https://www.alphafoodie.com/wp-content/uploads/2020/11/Orange-Juice-1-of-1.jpeg"),
                createProduct("Green Tea", "Healthy unsweetened green tea.", new BigDecimal("1.50"), category, "https://www.nfcr.org/wp-content/webp-express/webp-images/uploads/2019/08/Does-Green-Tea-Reduce-the-Risk-of-Cancer_.jpg.webp"),
                createProduct("Mineral Water", "Pure natural mineral water.", new BigDecimal("0.80"), category, "https://king-mart.shop:3010/media/file/crm/uploadfile/1728291756bb70424d9820191514c7bc2a982656cc4.jpg")
        );
    }

    private List<Product> createSnacks(Category category) {
        return List.of(
                createProduct("Potato Chips", "Crispy salted chips.", new BigDecimal("1.20"), category, "https://www.allrecipes.com/thmb/WyCC-RL8cuAEKfYHsdnzqi64iTc=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/73135-homestyle-potato-chips-ddmfs-0348-3x4-hero-c21021303c8849bbb40c1007bfa9af6e.jpg"),
                createProduct("Chocolate Bar", "Delicious milk chocolate.", new BigDecimal("1.50"), category, "https://thepodchocolates.co.uk/cdn/shop/files/hazelnutdarkarty2copy_d53a92cd-34fd-4dfb-b701-cb907502f443.jpg?v=1691923195&width=1946"),
                createProduct("Biscuits", "Crunchy butter biscuits.", new BigDecimal("1.10"), category, "https://ichef.bbci.co.uk/food/ic/food_16x9_1600/recipes/kenyan_tea_biscuits_97482_16x9.jpg"),
                createProduct("Popcorn", "Tasty caramel popcorn.", new BigDecimal("1.40"), category, "https://www.simplyrecipes.com/thmb/Xzggu-Md45HKhhYSw4DK8tGlZ_I=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Perfect-Popcorn-LEAD-41-4a75a18443ae45aa96053f30a3ed0a6b.JPG")
        );
    }

    private List<Product> createDairy(Category category) {
        return List.of(
                createProduct("Fresh Milk", "High-quality full cream milk.", new BigDecimal("1.90"), category, "https://media.makrocambodiaclick.com/PRODUCT_1671974881286.jpeg"),
                createProduct("Yogurt", "Delicious fruit-flavored yogurt.", new BigDecimal("1.30"), category, "https://nutritionsource.hsph.harvard.edu/wp-content/uploads/2024/11/AdobeStock_294324791-1024x681.jpeg"),
                createProduct("Cheese", "Premium cheddar cheese.", new BigDecimal("3.50"), category, "https://cabotcreamery.com/cdn/shop/articles/AdobeStock_130281036-5376x4214-b0eeb384-e4a9-4b9b-bc90-cdbbf4d24b40_1200x.jpg?v=1684341151"),
                createProduct("Butter", "Creamy natural butter.", new BigDecimal("2.40"), category, "https://cdn.britannica.com/27/122027-050-EAA86783/Butter.jpg")
        );
    }


    private Product createProduct(String name, String description, BigDecimal price, Category category, String imageUrl) {
        Product product = new Product();
        product.setUuid(UUID.randomUUID().toString());
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setIsActive(true);
        product.setIsDeleted(false);
        return product;
    }
}
