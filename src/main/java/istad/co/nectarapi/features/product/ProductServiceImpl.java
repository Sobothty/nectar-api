package istad.co.nectarapi.features.product;

import istad.co.nectarapi.audit.AuditResponse;
import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.domain.Product;
import istad.co.nectarapi.features.category.CategoryRepository;
import istad.co.nectarapi.features.product.dto.ProductRequest;
import istad.co.nectarapi.features.product.dto.ProductResponse;
import istad.co.nectarapi.features.product.dto.ProductUpdate;
import istad.co.nectarapi.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> !product.getIsDeleted())
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductByUuid(String uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (!categoryRepository.existsByUuid(productRequest.categoryUuid())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with uuid: " + productRequest.categoryUuid());
        }
        if (productRepository.existsByName(productRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product name already exists");
        }
        Product product = productMapper.fromProductRequest(productRequest);
        product.setUuid(UUID.randomUUID().toString());
        product.setIsDeleted(false);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(String uuid, ProductUpdate productUpdate) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Product not found with uuid: " + uuid));
        productMapper.updateProductFromRequest(productUpdate, product);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Transactional
    @Override
    public BasedMessage deleteProductByUuid(String uuid) {
        if (!productRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with uuid: " + uuid);
        }
        productRepository.deleteSoftByUuid(uuid);
        return new BasedMessage("Product deleted successfully");
    }
}
