package com.mtogo.customer.infrastructure.config;

import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.infrastructure.adapter.out.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityConfigTest.TestController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, SecurityConfigTest.TestController.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenProviderPort tokenProviderPort;

    @Test
    void protectedEndpointAllowsWhenJwtValid() throws Exception {
        when(tokenProviderPort.validateAndGetUserId("good-token")).thenReturn(1L);

        mockMvc.perform(get("/protected/test")
                        .header("Authorization", "Bearer good-token")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("protected-ok"));
    }

    @Test
    void protectedEndpointRejectsWithoutJwt() throws Exception {
        mockMvc.perform(get("/protected/test"))
                .andExpect(status().isUnauthorized());
    }

    @RestController
    public static class TestController {
        @GetMapping("/protected/test")
        public String secured() {
            return "protected-ok";
        }
    }
}
