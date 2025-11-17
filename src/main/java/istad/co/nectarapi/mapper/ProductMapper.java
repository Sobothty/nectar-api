package istad.co.nectarapi.mapper;

import istad.co.nectarapi.audit.AuditMapper;
import istad.co.nectarapi.domain.Product;
import istad.co.nectarapi.features.product.dto.ProductRequest;
import istad.co.nectarapi.features.product.dto.ProductResponse;
import istad.co.nectarapi.features.product.dto.ProductUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AuditMapper.class, MapperHelper.class})
public interface ProductMapper {

    @Mapping(target = "audit", source = "product")
    @Mapping(target = "category", source = "category.name")
    ProductResponse toProductResponse(Product product);

    @Mapping(source = "categoryUuid", target = "category", qualifiedByName = "toCategoryName")
    Product fromProductRequest(ProductRequest productRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromRequest(ProductUpdate productUpdate, @MappingTarget Product product);
}
