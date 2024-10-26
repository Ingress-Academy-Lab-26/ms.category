package az.ingress.mscategory.dao.repository

import az.ingress.mscategory.dao.entity.CategoryEntity
import az.ingress.mscategory.model.enums.CategoryStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CategoryRepositoryTest extends Specification {

    @Autowired
    CategoryRepository categoryRepository

    def "findByParentIdAndStatus should return categories with matching parentId and status"() {
        given:
        def parentCategory = new CategoryEntity(name: "Parent Category", parentId: null, status: CategoryStatus.ACTIVE)
        def childCategory1 = new CategoryEntity(name: "Child Category 1", parentId: 1L, status: CategoryStatus.ACTIVE)

        categoryRepository.save(parentCategory)
        categoryRepository.save(childCategory1)

        when:
        def result = categoryRepository.findByParentIdAndStatus(1L, CategoryStatus.ACTIVE)

        then:
        // Verify that only the matching category is returned
        result.size() == 1
        result[0].name == "Child Category 1"
        result[0].status == CategoryStatus.ACTIVE
    }
}

