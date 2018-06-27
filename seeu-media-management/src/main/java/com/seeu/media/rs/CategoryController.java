package com.seeu.media.rs;

import com.seeu.media.category.CategoryEntity;
import com.seeu.media.category.CategoryService;
import com.seeu.media.rs.dto.CategoryDTO;
import com.seeu.media.rs.exception.CategoryNameIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/medias/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryEntity createNewCategory(@RequestBody CategoryDTO categoryDTO) {

        if (categoryDTO.getName() == null) {
            throw new CategoryNameIsNullException("Category name is null");
        }

        return categoryService.createCategory(categoryDTO);
    }

    @GetMapping
    public CategoryEntity getCategoryInfo(@RequestParam Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PutMapping
    public ResponseEntity updateCategoryInfo(@RequestBody CategoryDTO category) {
        categoryService.updateCategoryName(category.getIdCategory(), category.getName());
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteCategory(@RequestParam Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * DEBUG
     * @return
     */
    @GetMapping("/list")
    public List<CategoryEntity> listTags() {
        return categoryService.getAllCategories();
    }

}
