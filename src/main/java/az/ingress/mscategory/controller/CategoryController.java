package az.ingress.mscategory.controller;

import az.ingress.mscategory.model.response.CategoryDto;
import az.ingress.mscategory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoryService.getCategoriesWithSubCategories();
    }

}

