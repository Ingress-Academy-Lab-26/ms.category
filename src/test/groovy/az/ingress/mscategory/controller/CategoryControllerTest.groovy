package az.ingress.mscategory.controller

import az.ingress.mscategory.exception.AuthException
import az.ingress.mscategory.exception.ErrorHandler
import az.ingress.mscategory.exception.ExceptionConstraints
import az.ingress.mscategory.exception.NotFoundException
import az.ingress.mscategory.model.request.CategoryRequest
import az.ingress.mscategory.model.request.CategoryUpdateRequest
import az.ingress.mscategory.model.response.CategoryTreeNodeResponse
import az.ingress.mscategory.service.abstracts.AuthService
import az.ingress.mscategory.service.concrete.CategoryServiceHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import spock.lang.Specification

import static az.ingress.mscategory.exception.ExceptionConstraints.USER_UNAUTHORIZED_CODE
import static az.ingress.mscategory.exception.ExceptionConstraints.USER_UNAUTHORIZED_MESSAGE
import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class CategoryControllerTest extends Specification {
    CategoryServiceHandler categoryServiceHandler
    AuthService authService
    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()

    void setup() {
        categoryServiceHandler = Mock()
        authService = Mock()
        def categoryController = new CategoryController(categoryServiceHandler, authService)
        def validatorFactory = new LocalValidatorFactoryBean()
        validatorFactory.afterPropertiesSet()
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ErrorHandler())
                .setValidator(validatorFactory)
                .build()
    }

    def "getCategories() method should return list of categories"() {
        given:
        def categoryResponse = new CategoryTreeNodeResponse(id: 1L, name: "CategoryName", baseId: null, picture: "picture.jpg", subCategories: [])
        def expectedJson = objectMapper.writeValueAsString([categoryResponse])
        categoryServiceHandler.getCategories() >> [categoryResponse]
        authService.isValidToken("Bearer valid-token") >> true

        when:
        def response = mockMvc.perform(get("/v1/categories")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
    }


    def "createCategory() method should create categories and return HTTP 201"() {
        given: "a valid CategoryRequest"
        def categoryDetail1 = new CategoryRequest.CategoryDetail(name: "Category 1", baseId: null, picture: "pic1.png")
        def categoryDetail2 = new CategoryRequest.CategoryDetail(name: "Category 2", baseId: 1L, picture: "pic2.png")
        def categoryRequest = new CategoryRequest(categories: [categoryDetail1, categoryDetail2])

        and: "the request body as JSON"
        def requestBody = objectMapper.writeValueAsString(categoryRequest)

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "POST request is made to /categories endpoint"
        def result = mockMvc.perform(post("/v1/categories")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        then: "the response status is 201 CREATED"
        result.andExpect(status().isCreated())

        and: "categoryService.createCategory is called with the correct parameters"
        1 * categoryServiceHandler.createCategory(_ as CategoryRequest) >> { CategoryRequest request ->
            assert request.categories.size() == 2
            assert request.categories[0].name == "Category 1"
            assert request.categories[1].name == "Category 2"
        }
    }


    def "createCategory() method should return HTTP 400 when categories list is null"() {
        given: "an invalid CategoryRequest with null categories"
        def categoryRequest = new CategoryRequest(categories: null)

        and: "the request body as JSON"
        def requestBody = objectMapper.writeValueAsString(categoryRequest)

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "POST request is made to /v1/categories endpoint"
        def result = mockMvc.perform(post("/v1/categories")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        then: "the response status is 400 BAD REQUEST"
        result.andExpect(status().isBadRequest())

        and: "categoryService.createCategory is not called"
        0 * categoryServiceHandler.createCategory(_)
    }

    def "createCategory() method should return HTTP 400 when required fields are missing"() {
        given: "an invalid CategoryRequest with missing name and picture"
        def categoryRequest = new CategoryRequest(categories: null)

        and: "the request body as JSON"
        def requestBody = objectMapper.writeValueAsString(categoryRequest)

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "POST request is made to /v1/categories endpoint"
        def result = mockMvc.perform(post("/v1/categories")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        then: "the response status is 400 BAD REQUEST"
        result.andExpect(status().isBadRequest())

        and: "categoryService.createCategory is not called"
        0 * categoryServiceHandler.createCategory(_)

        and: "response contains validation error details"
        result.andExpect(jsonPath('$.code').value("VALIDATION_EXCEPTION"))
                .andExpect(jsonPath('$.message').value("[categories: Categories list cannot be null]"))
    }

    def "createCategory() method should return HTTP 401 when Authorization token is invalid"() {
        given: "a valid CategoryRequest"
        def categoryDetail = new CategoryRequest.CategoryDetail(name: "Invalid Category", baseId: null, picture: "invalid.png")
        def categoryRequest = new CategoryRequest(categories: [categoryDetail])

        and: "the request body as JSON"
        def requestBody = objectMapper.writeValueAsString(categoryRequest)

        and: "authService throws an AuthException for invalid token"
        authService.validateToken("Bearer invalid-token") >> { throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE) }

        when: "POST request is made to /v1/categories endpoint with an invalid token"
        def result = mockMvc.perform(post("/v1/categories")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        then: "the response status is 401 UNAUTHORIZED"
        result.andExpect(status().isUnauthorized())

        and: "categoryService.createCategory is not called"
        0 * categoryServiceHandler.createCategory(_)
    }


    def "updateCategory() method should return HTTP 204 NO_CONTENT when category is successfully updated"() {
        given: "a valid CategoryUpdateRequest"
        def categoryUpdateRequest = new CategoryUpdateRequest(name: "Updated Category", baseId: 1L, picture: "updated-pic.png")
        def requestBody = objectMapper.writeValueAsString(categoryUpdateRequest)

        and: "a valid categoryId"
        def categoryId = 1L

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "PUT request is made to /v1/categories/{categoryId} endpoint"
        def result = mockMvc.perform(put("/v1/categories/$categoryId")
                .header("Authorization", "Bearer valid-token")
                .contentType("application/json")
                .content(requestBody))

        then: "the response status is 204 NO_CONTENT"
        result.andExpect(status().isNoContent())

        and: "categoryService.updateCategory is called with correct parameters"
        1 * categoryServiceHandler.updateCategory(categoryId, _ as CategoryUpdateRequest)
    }

    def "updateCategory() method should return HTTP 400 with VALIDATION_EXCEPTION code when required fields are missing"() {
        given: "an invalid CategoryUpdateRequest with blank name and picture"
        def categoryUpdateRequest = new CategoryUpdateRequest(name: "", baseId: 1L, picture: "")
        def requestBody = objectMapper.writeValueAsString(categoryUpdateRequest)

        and: "a valid categoryId"
        def categoryId = 1L

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "PUT request is made to /v1/categories/{categoryId} endpoint"
        def result = mockMvc.perform(put("/v1/categories/$categoryId")
                .header("Authorization", "Bearer valid-token")
                .contentType("application/json")
                .content(requestBody))

        then: "the response status is 400 BAD_REQUEST"
        result.andExpect(status().isBadRequest())

        and: "the response contains the expected validation error structure"
        result.andExpect(jsonPath('$.code').value("VALIDATION_EXCEPTION"))
                .andExpect(jsonPath('$.message').value(containsString("name: Category name cannot be blank")))
                .andExpect(jsonPath('$.message').value(containsString("picture: Category picture cannot be blank")))
    }

    def "updateCategory() method should return HTTP 401 when Authorization token is invalid"() {
        given: "a valid CategoryUpdateRequest"
        def categoryUpdateRequest = new CategoryUpdateRequest(name: "Invalid Category", baseId: 1L, picture: "invalid.png")
        def requestBody = objectMapper.writeValueAsString(categoryUpdateRequest)

        and: "a valid categoryId"
        def categoryId = 1L

        and: "authService throws an AuthException for invalid token"
        authService.validateToken("Bearer invalid-token") >> { throw new AuthException(USER_UNAUTHORIZED_MESSAGE, USER_UNAUTHORIZED_CODE) }

        when: "PUT request is made to /v1/categories/{categoryId} endpoint with an invalid token"
        def result = mockMvc.perform(put("/v1/categories/$categoryId")
                .header("Authorization", "Bearer invalid-token")
                .contentType("application/json")
                .content(requestBody))

        then: "the response status is 401 UNAUTHORIZED"
        result.andExpect(status().isUnauthorized())

        and: "categoryService.updateCategory is not called"
        0 * categoryServiceHandler.updateCategory(_, _)
    }


    def "deleteCategory() method should return HTTP 204 NO_CONTENT when category is successfully deleted"() {
        given: "a valid categoryId"
        def categoryId = 1L

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        when: "DELETE request is made to /v1/categories/{categoryId} endpoint"
        def result = mockMvc.perform(delete("/v1/categories/$categoryId")
                .header("Authorization", "Bearer valid-token"))


        then: "the response status is 204 NO_CONTENT"
        result.andExpect(status().isNoContent())

        and: "categoryService.deleteCategory is called with the correct categoryId"
        1 * categoryServiceHandler.deleteCategory(categoryId)
    }

    def "deleteCategory() method should return HTTP 404 NOT_FOUND when category does not exist"() {
        given: "a non-existent categoryId"
        def categoryId = 999L

        and: "authServiceClient returns true for token validation"
        authService.isValidToken("Bearer valid-token") >> true

        and: "categoryService throws NotFoundException for the given categoryId"
        categoryServiceHandler.deleteCategory(categoryId) >> { throw new NotFoundException("Category not found", "CATEGORY_NOT_FOUND") }

        when: "DELETE request is made to /v1/categories/{categoryId} endpoint"
        def result = mockMvc.perform(delete("/v1/categories/$categoryId")
                .header("Authorization", "Bearer valid-token"))

        then: "the response status is 404 NOT_FOUND"
        result.andExpect(status().isNotFound())

        and: "response contains error details from ExceptionResponse"
        result.andExpect(jsonPath('$.code').value("CATEGORY_NOT_FOUND"))
                .andExpect(jsonPath('$.message').value("Category not found"))
    }

    def "deleteCategory() method should return HTTP 401 when Authorization token is invalid"() {
        given: "a valid categoryId"
        def categoryId = 1L

        and: "authService throws an AuthException for invalid token"
        authService.validateToken("Bearer invalid-token") >> { throw new AuthException(USER_UNAUTHORIZED_MESSAGE, USER_UNAUTHORIZED_CODE) }

        when: "DELETE request is made to /v1/categories/{categoryId} endpoint with an invalid token"
        def result = mockMvc.perform(delete("/v1/categories/$categoryId")
                .header("Authorization", "Bearer invalid-token"))

        then: "the response status is 401 UNAUTHORIZED"
        result.andExpect(status().isUnauthorized())

        and: "categoryService.deleteCategory is not called"
        0 * categoryServiceHandler.deleteCategory(_)
    }
}