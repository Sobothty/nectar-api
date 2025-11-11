package istad.co.nectarapi.features.category;

import istad.co.nectarapi.features.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

}
