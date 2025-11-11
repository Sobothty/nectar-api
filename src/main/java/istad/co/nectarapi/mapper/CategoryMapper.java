package istad.co.nectarapi.mapper;

import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.features.category.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "audit", source = "category")
    CategoryResponse toCategoryResponse(Category category);
}
