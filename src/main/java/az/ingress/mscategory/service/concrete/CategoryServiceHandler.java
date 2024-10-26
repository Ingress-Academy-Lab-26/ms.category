package az.ingress.mscategory.service.concrete;

import az.ingress.mscategory.aspect.Log;
import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.exception.CannotDeleteSubCategoryException;
import az.ingress.mscategory.exception.NotFoundException;
import az.ingress.mscategory.model.request.CategoryRequest;
import az.ingress.mscategory.model.request.CategoryUpdateRequest;
import az.ingress.mscategory.model.response.CategoryTreeNodeResponse;
import az.ingress.mscategory.service.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static az.ingress.mscategory.exception.ExceptionConstraints.CANNOT_DELETE_SUBCATEGORY_CODE;
import static az.ingress.mscategory.exception.ExceptionConstraints.CANNOT_DELETE_SUBCATEGORY_MESSAGE;
import static az.ingress.mscategory.exception.ExceptionConstraints.CATEGORY_NOT_FOUND_CODE;
import static az.ingress.mscategory.exception.ExceptionConstraints.CATEGORY_NOT_FOUND_MESSAGE;
import static az.ingress.mscategory.mapper.factory.CategoryMapper.CATEGORY_MAPPER;

@Log
@Service
@RequiredArgsConstructor
public class CategoryServiceHandler implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    @Override
    public List<CategoryTreeNodeResponse> getCategories() {
        var categoriesFromCache = cacheService.getCategoriesFromCache();
        if (categoriesFromCache == null || categoriesFromCache.isEmpty()) {
            categoriesFromCache = categoryRepository.findAll();
            cacheService.saveCategoriesToCache(categoriesFromCache);
        }
        return CATEGORY_MAPPER.buildCategoryTree(categoriesFromCache);
    }


    @Transactional
    @Override
    public void createCategory(CategoryRequest categoryRequest) {
        var separationResult = CATEGORY_MAPPER.separateCategories(categoryRequest.getCategories());
        CATEGORY_MAPPER.saveBaseCategories(separationResult.getBaseCategories());
        CATEGORY_MAPPER.saveSubCategories(separationResult.getSubCategories());
        var allCategories = categoryRepository.findAll();
        cacheService.saveCategoriesToCache(allCategories);
    }

    @Transactional
    @Override
    public void updateCategory(Long categoryId, CategoryUpdateRequest categoryUpdateRequest) {
        var categoryEntity = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_MESSAGE, CATEGORY_NOT_FOUND_CODE));
        CATEGORY_MAPPER.updateCategoryDetails(categoryEntity, categoryUpdateRequest);
        categoryRepository.save(categoryEntity);
        var allCategories = categoryRepository.findAll();
        cacheService.saveCategoriesToCache(allCategories);
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        var categoryEntity = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(
                CATEGORY_NOT_FOUND_MESSAGE + categoryId, CATEGORY_NOT_FOUND_CODE));
        if (categoryEntity.getBaseId() == null) {
            var subCategories = categoryRepository.findByBaseId(categoryId);
            categoryRepository.deleteAll(subCategories);
        } else {
            throw new CannotDeleteSubCategoryException(CANNOT_DELETE_SUBCATEGORY_MESSAGE + categoryId, CANNOT_DELETE_SUBCATEGORY_CODE);
        }
        categoryRepository.deleteById(categoryId);
        var allCategories = categoryRepository.findAll();
        cacheService.saveCategoriesToCache(allCategories);
    }
}

