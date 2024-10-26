package az.ingress.mscategory.controller;

import az.ingress.mscategory.model.request.CategoryRequest;
import az.ingress.mscategory.model.request.CategoryUpdateRequest;
import az.ingress.mscategory.model.response.CategoryTreeNodeResponse;
import az.ingress.mscategory.service.abstracts.AuthService;
import az.ingress.mscategory.service.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    @GetMapping
    @PreAuthorize("@authService.verifyToken(#accessToken)")
    public List<CategoryTreeNodeResponse> getCategories(@RequestHeader(AUTHORIZATION) String accessToken) {
        return categoryService.getCategories();
    }

    @PostMapping
    @PreAuthorize("@authService.verifyToken(#accessToken)")
    @ResponseStatus(CREATED)
    public void createCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                                               @RequestBody @Valid CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("@authService.verifyToken(#accessToken)")
    @ResponseStatus(NO_CONTENT)
    public void updateCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                                               @PathVariable Long categoryId, @RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        categoryService.updateCategory(categoryId, categoryUpdateRequest);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("@authService.verifyToken(#accessToken)")
    @ResponseStatus(NO_CONTENT)
    public void deleteCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                                               @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}

