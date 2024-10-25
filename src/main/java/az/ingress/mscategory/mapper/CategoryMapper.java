package az.ingress.mscategory.mapper;

import az.ingress.mscategory.dao.entity.CategoryEntity;
import az.ingress.mscategory.model.response.CategoryDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class CategoryMapper {

    public abstract CategoryDto map(CategoryEntity entity);

}
