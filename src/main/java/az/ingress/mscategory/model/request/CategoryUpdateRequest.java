package az.ingress.mscategory.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    @NotBlank(message = "Category name cannot be blank")
    @NotNull(message = "Category name cannot be null")
    private String name;
    private Long baseId;
    @NotBlank(message = "Category picture cannot be blank")
    @NotNull(message = "Category picture cannot be null")
    private String picture;
}
