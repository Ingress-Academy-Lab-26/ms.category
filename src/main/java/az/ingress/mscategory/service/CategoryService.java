package az.ingress.mscategory.service;

import az.ingress.mscategory.annotation.Log;
import az.ingress.mscategory.dao.entity.CategoryEntity;
import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.model.response.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Log
    public List<CategoryDto> getCategoriesWithSubCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        Map<Long, CategoryDto> categoryMap = new HashMap<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(categoryEntity.getId());
            categoryDto.setName(categoryEntity.getName());
            categoryDto.setChildren(new ArrayList<>());
            categoryMap.put(categoryEntity.getId(), categoryDto);
        }
        List<CategoryDto> rootCategories = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            if (categoryEntity.getParentId() == null) {
                rootCategories.add(categoryMap.get(categoryEntity.getId()));
            } else {
                CategoryDto parentCategory = categoryMap.get(categoryEntity.getParentId());
                if (parentCategory != null) {
                    parentCategory.getChildren().add(categoryMap.get(categoryEntity.getId()));
                }
            }
        }
        return rootCategories;
    }


}

