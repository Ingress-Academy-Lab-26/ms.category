package az.ingress.mscategory.dao.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "categories")
public class CategoryDocument {
    @Id
    ObjectId id;
    String title;
    Integer productAmount;
    Boolean adult;
    Boolean eco;
    String seoHeader;
    String seoMetaTag;
    List<Long> path;
    List<CategoryDocument> children;
}


