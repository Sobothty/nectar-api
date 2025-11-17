package istad.co.nectarapi.features.product;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.features.product.dto.ProductRequest;
import istad.co.nectarapi.features.product.dto.ProductResponse;
import istad.co.nectarapi.features.product.dto.ProductUpdate;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductByUuid(String uuid);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(String uuid, ProductUpdate productUpdate);

    BasedMessage deleteProductByUuid(String uuid);

}
