package az.ingress.mscategory.dao.repository;

import az.ingress.mscategory.dao.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {}

