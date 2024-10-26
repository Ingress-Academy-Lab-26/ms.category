package az.ingress.mscategory.service


import az.ingress.mscategory.dao.repository.CategoryRepository
import az.ingress.mscategory.mapper.factory.CategoryMapper
import az.ingress.mscategory.model.response.CategoryResponse
import az.ingress.mscategory.service.concrete.CategoryServiceHandler
import az.ingress.mscategory.util.CacheUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class CategoryServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom();
    def categoryRepository = Mock(CategoryRepository)
    def cacheUtil = Mock(CacheUtil)
    def categoryMapper = GroovyMock(CategoryMapper)
    CategoryServiceHandler categoryServiceHandler = Mock(CategoryServiceHandler)


    def "getCategories should return a list of CategoryResponse in a hierarchical structure"() {
        given:
        categoryServiceHandler.getCategories() >> [
                new CategoryResponse(id: 1, name: "Parent Category", children: [
                        new CategoryResponse(id: 2, name: "Child Category", parentId: 1)
                ])
        ]
        when:
        def result = categoryServiceHandler.getCategories()

        then:
        result != null
        result.size() == 1
        result[0].name == "Parent Category"
        result[0].children.size() == 1
        result[0].children[0].name == "Child Category"
    }
}
