package az.ingress.mscategory.controller

import az.ingress.mscategory.exception.ErrorHandler
import az.ingress.mscategory.model.request.CategoryRequest
import az.ingress.mscategory.model.request.CategoryUpdateRequest
import az.ingress.mscategory.model.response.CategoryResponse
import az.ingress.mscategory.service.concrete.CategoryServiceHandler
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CategoryControllerTest extends Specification {
    private CategoryServiceHandler categoryServiceHandler
    private MockMvc mockMvc
    private ObjectMapper objectMapper = new ObjectMapper()

    void setup() {
        categoryServiceHandler = Mock()
        def categoryController = new CategoryController(categoryServiceHandler)
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "getCategories should return list of categories"() {
        given:
        def categoryResponse = new CategoryResponse(id: 1L, name: "CategoryName", parentId: null, picture: "picture.jpg", children: [])
        def expectedJson = objectMapper.writeValueAsString([categoryResponse])
        categoryServiceHandler.getCategories() >> [categoryResponse]

        when:
        def response = mockMvc.perform(get("/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
    }

    def "createCategory should return list of created categories for bulk request"() {
        given:
        def categoryDetails = [
                new CategoryRequest.CategoryDetail(name: "Category 1", parentId: null, picture: "picture1.jpg"),
                new CategoryRequest.CategoryDetail(name: "Category 2", parentId: 1L, picture: "picture2.jpg")
        ]
        def categoryRequest = new CategoryRequest(categories: categoryDetails)
        def requestJson = objectMapper.writeValueAsString(categoryRequest)

        def categoryResponses = [
                new CategoryResponse(id: 1L, name: "Category 1", parentId: null, picture: "picture1.jpg", children: []),
                new CategoryResponse(id: 2L, name: "Category 2", parentId: 1L, picture: "picture2.jpg", children: [])
        ]
        def expectedJson = objectMapper.writeValueAsString(categoryResponses)

        categoryServiceHandler.createCategory(_ as CategoryRequest) >> categoryResponses

        when:
        def response = mockMvc.perform(post("/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isCreated())
                .andExpect(content().json(expectedJson))
    }

    def "updateCategory should return updated category with status NO_CONTENT"() {
        given:
        def categoryUpdateRequest = new CategoryUpdateRequest(name: "Updated Category", picture: "updated_picture.jpg")
        def requestJson = objectMapper.writeValueAsString(categoryUpdateRequest)
        def updatedCategoryResponse = new CategoryResponse(id: 1L, name: "Updated Category", parentId: null, picture: "updated_picture.jpg", children: [])
        categoryServiceHandler.updateCategory(1L, _ as CategoryUpdateRequest) >> updatedCategoryResponse

        when:
        def response = mockMvc.perform(put("/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))

        then:
        response.andExpect(status().isNoContent())
                .andExpect(jsonPath('$.id').value(1L))
                .andExpect(jsonPath('$.name').value("Updated Category"))
                .andExpect(jsonPath('$.picture').value("updated_picture.jpg"))
    }

    def "deleteCategory should return NO_CONTENT status"() {
        given:
        Long categoryId = 1L
        categoryServiceHandler.deleteCategory(categoryId) >> null

        when:
        def response = mockMvc.perform(delete("/v1/categories/$categoryId")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isNoContent())

        and:
        1 * categoryServiceHandler.deleteCategory(categoryId)
    }
}