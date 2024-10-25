package az.ingress.mscategory.dao.repository;

import az.ingress.mscategory.dao.entity.CategoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryDocument, Long> {}

