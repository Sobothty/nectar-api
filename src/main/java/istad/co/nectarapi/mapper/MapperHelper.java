package istad.co.nectarapi.mapper;

import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.domain.Product;
import istad.co.nectarapi.features.category.CategoryRepository;
import istad.co.nectarapi.features.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class MapperHelper {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Named("toCategoryName")
    public Category toCategoryName(String categoryUuid) {
        return categoryRepository.findByUuid(categoryUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    @Named("toProductName")
    public Product toProductName(String productUuid) {
        return productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
