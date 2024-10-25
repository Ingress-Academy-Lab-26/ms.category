package az.ingress.mscategory.service;

import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.mapper.CategoryMapper;
import az.ingress.mscategory.model.response.CategoryDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<CategoryDto> getRootCategories() {
        var categoryEntityList = categoryRepository.findAll().stream().filter(categoryDocument -> categoryDocument.getChildren() != null && !categoryDocument.getChildren().isEmpty()).toList();
        return categoryEntityList.stream()
                .map(categoryMapper::map)
                .toList();
    }

}

