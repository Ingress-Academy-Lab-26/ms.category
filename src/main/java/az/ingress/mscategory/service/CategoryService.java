package az.ingress.mscategory.service;

import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.mapper.CategoryMapper;
import az.ingress.mscategory.model.response.CategoryDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getRootCategories() {
        var categoryEntityList = categoryRepository.findAll().stream().filter(categoryDocument -> categoryDocument.getChildren() != null && !categoryDocument.getChildren().isEmpty()).toList();
        return categoryEntityList.stream()
                .map(categoryMapper::map)
                .toList();
    }

}

