package az.ingress.mscategory.controller

import az.ingress.mscategory.controller.HealthController
import az.ingress.mscategory.exception.ErrorHandler
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class HealthControllerTest extends Specification {
    HealthController healthController
    MockMvc mockMvc

    def setup() {
        healthController = new HealthController()
        mockMvc = standaloneSetup(healthController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "TestCheckHealthy"() {
        given:
        def url = "/health"

        when:
        def actual = mockMvc.perform(get(url))

        then:
        actual.andExpect(
                status().isOk()
        )
    }
}