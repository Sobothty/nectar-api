package istad.co.nectarapi.mapper;

import istad.co.nectarapi.audit.AuditMapper;
import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.features.category.dto.CategoryRequest;
import istad.co.nectarapi.features.category.dto.CategoryResponse;
import istad.co.nectarapi.features.category.dto.CategoryUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AuditMapper.class})
public interface CategoryMapper {

    @Mapping(target = "audit", source = "category")
    CategoryResponse toCategoryResponse(Category category);


    Category toCategoryRequest(CategoryRequest categoryRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void categoryUpdate(CategoryUpdate categoryUpdate, @MappingTarget Category category);
}
