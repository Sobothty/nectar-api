package istad.co.nectarapi.features.product;


import istad.co.nectarapi.features.product.dto.ProductRequest;
import istad.co.nectarapi.features.product.dto.ProductUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(
                Map.of("products", productService.getAllProducts()), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getProductByUuid(@PathVariable String uuid) {
        return new ResponseEntity<>(
                productService.getProductByUuid(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String uuid,
            @RequestBody @Valid ProductUpdate productUpdate
            ) {
        return new ResponseEntity<>(
                productService.updateProduct(uuid, productUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteProductByUuid(@PathVariable String uuid) {
        return new ResponseEntity<>(
                productService.deleteProductByUuid(uuid), HttpStatus.OK);
    }
}
