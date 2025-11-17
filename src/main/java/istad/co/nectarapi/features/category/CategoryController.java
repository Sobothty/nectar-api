package istad.co.nectarapi.features.category;

import istad.co.nectarapi.features.category.dto.CategoryRequest;
import istad.co.nectarapi.features.category.dto.CategoryUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return new ResponseEntity<>(
                Map.of("categories", categoryService.getAllCategories()), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getCategoryByUuid(@PathVariable String uuid) {
        return new ResponseEntity<>(categoryService.getCategoryByUuid(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return new ResponseEntity<>(
                categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateCategory(@PathVariable String uuid, @RequestBody @Valid CategoryUpdate categoryUpdate) {
        return new ResponseEntity<>(
                categoryService.updateCategory(uuid, categoryUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteCategory(@PathVariable String uuid) {
        return new ResponseEntity<>(
                categoryService.deleteCategory(uuid), HttpStatus.OK);
    }

}
