package az.ingress.mscategory.mapper.factory;

import az.ingress.mscategory.dao.entity.CategoryEntity;
import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.exception.NotFoundException;
import az.ingress.mscategory.model.request.CategoryRequest;
import az.ingress.mscategory.model.request.CategoryUpdateRequest;
import az.ingress.mscategory.model.response.CategorySeparationResult;
import az.ingress.mscategory.model.response.CategoryTreeNodeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static az.ingress.mscategory.exception.ExceptionConstraints.CATEGORY_NOT_FOUND_CODE;
import static az.ingress.mscategory.exception.ExceptionConstraints.CATEGORY_NOT_FOUND_MESSAGE;

public enum CategoryMapper {
    CATEGORY_MAPPER;
    CategoryRepository categoryRepository;

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategorySeparationResult separateCategories(List<CategoryRequest.CategoryDetail> categories) {
        var baseCategories = new ArrayList<CategoryRequest.CategoryDetail>();
        var subCategories = new ArrayList<CategoryRequest.CategoryDetail>();
        categories.forEach(categoryDetail -> {
            if (categoryDetail.getBaseId() == null) {
                baseCategories.add(categoryDetail);
            } else
                subCategories.add(categoryDetail);
        });
        return new CategorySeparationResult(baseCategories, subCategories);
    }

    public void saveBaseCategories(List<CategoryRequest.CategoryDetail> baseCategories) {
        categoryRepository.saveAll(baseCategories.stream()
                .map(baseCategory -> CategoryEntity.builder()
                        .name(baseCategory.getName())
                        .picture(baseCategory.getPicture())
                        .baseId(null)
                        .build())
                .collect(Collectors.toList()));
    }

    public void saveSubCategories(List<CategoryRequest.CategoryDetail> subCategories) {
        categoryRepository.saveAll(subCategories.stream()
                .map(baseCategory -> CategoryEntity.builder()
                        .name(baseCategory.getName())
                        .picture(baseCategory.getPicture())
                        .baseId(null)
                        .build())
                .collect(Collectors.toList()));
    }

    public void updateCategoryDetails(CategoryEntity categoryEntity, CategoryUpdateRequest categoryUpdateRequest) {
        categoryEntity.setName(categoryUpdateRequest.getName());
        categoryEntity.setPicture(categoryUpdateRequest.getPicture());
        if (categoryUpdateRequest.getBaseId() != null) {
            categoryRepository.findById(categoryUpdateRequest.getBaseId()).orElseThrow(() -> new NotFoundException(
                    CATEGORY_NOT_FOUND_MESSAGE + categoryUpdateRequest.getBaseId(), CATEGORY_NOT_FOUND_CODE));
            categoryEntity.setBaseId(categoryUpdateRequest.getBaseId());
        } else {
            categoryEntity.setBaseId(null);
        }
    }

    public List<CategoryTreeNodeResponse> buildCategoryTree(List<CategoryEntity> categories) {
        var subCategoryMap = categories.stream()
                .filter(categoryEntity -> categoryEntity.getBaseId() != null)
                .map(CategoryTreeNodeResponse::new)
                .collect(Collectors.groupingBy(CategoryTreeNodeResponse::getBaseId));
        return categories.stream()
                .filter(categoryEntity -> categoryEntity.getBaseId() == null)
                .map(base -> {
                    var baseNode = new CategoryTreeNodeResponse(base);
                    baseNode.setSubCategories(subCategoryMap.get(base.getId()));
                    return baseNode;
                })
                .collect(Collectors.toList());
    }
}

