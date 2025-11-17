package istad.co.nectarapi.features.category;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.features.category.dto.CategoryRequest;
import istad.co.nectarapi.features.category.dto.CategoryResponse;
import istad.co.nectarapi.features.category.dto.CategoryUpdate;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryByUuid(String uuid);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(String uuid, CategoryUpdate categoryUpdate);

    BasedMessage deleteCategory(String uuid);
}
