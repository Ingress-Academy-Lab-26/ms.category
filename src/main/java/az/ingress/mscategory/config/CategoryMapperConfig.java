package az.ingress.mscategory.config;

import az.ingress.mscategory.dao.repository.CategoryRepository;
import az.ingress.mscategory.mapper.factory.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryMapperConfig {

    @Autowired
    public void configure(CategoryRepository categoryRepository) {
        CategoryMapper.CATEGORY_MAPPER.setCategoryRepository(categoryRepository);
    }
}

