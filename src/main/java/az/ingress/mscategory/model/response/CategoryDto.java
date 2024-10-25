package az.ingress.mscategory.model.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;
}

