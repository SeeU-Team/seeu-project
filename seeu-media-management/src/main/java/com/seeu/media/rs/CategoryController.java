package com.seeu.media.rs;

import com.seeu.media.category.CategoryService;
import com.seeu.media.rs.dto.CategoryDto;
import com.seeu.media.rs.exception.CategoryValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medias/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryInfo(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto createNewCategory(@RequestBody @Valid CategoryDto categoryDto,
											BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new CategoryValidationException();
		}

		return categoryService.createCategory(categoryDto);
	}

    @PutMapping
    public ResponseEntity updateCategoryInfo(@RequestBody @Valid CategoryDto category,
											 BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new CategoryValidationException();
		}

        categoryService.updateCategoryName(category.getId(), category.getName());
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
