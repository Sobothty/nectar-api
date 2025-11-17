package istad.co.nectarapi.features.category;

import istad.co.nectarapi.base.BasedMessage;
import istad.co.nectarapi.domain.Category;
import istad.co.nectarapi.features.category.dto.CategoryRequest;
import istad.co.nectarapi.features.category.dto.CategoryResponse;
import istad.co.nectarapi.features.category.dto.CategoryUpdate;
import istad.co.nectarapi.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(category -> !category.getIsDeleted())
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryByUuid(String uuid) {
        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with UUID " + uuid + " not found"));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with name " + categoryRequest.name() + " already exists");
        }
        Category category = categoryMapper.toCategoryRequest(categoryRequest);
        category.setUuid(UUID.randomUUID().toString());
        category.setIsDeleted(false);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(String uuid, CategoryUpdate categoryUpdate) {
        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with UUID " + uuid + " not found"));
        categoryMapper.categoryUpdate(categoryUpdate, category);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Transactional
    @Override
    public BasedMessage deleteCategory(String uuid) {
        if (!categoryRepository.existsByUuid(uuid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with UUID " + uuid + " not found");
        }
        categoryRepository.deleteSoftByUuid(uuid);
        return new BasedMessage("Category with UUID " + uuid + " deleted successfully");
    }
}
