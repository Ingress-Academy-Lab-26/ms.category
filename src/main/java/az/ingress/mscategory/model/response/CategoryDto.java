package az.ingress.mscategory.model.response;

import org.bson.types.ObjectId;

import java.util.List;

public record CategoryDto(ObjectId id,
                          String title,
                          Integer productAmount,
                          Boolean adult,
                          Boolean eco,
                          String seoHeader,
                          List<Long> path,
                          List<CategoryDto> children) {}

