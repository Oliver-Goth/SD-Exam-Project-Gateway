package com.mtogo.restaurant.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityConfigTest.TestController.class)
@Import({ SecurityConfig.class, JwtAuthenticationFilter.class, SecurityConfigTest.TestController.class })
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void publicEndpointAllowsAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/restaurants/onboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void loginEndpointAllowsAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointAllowsWhenJwtValid() throws Exception {
        when(jwtTokenProvider.validateToken("good-token")).thenReturn(true);
        when(jwtTokenProvider.getRestaurantIdFromToken("good-token")).thenReturn(1L);

        mockMvc.perform(post("/api/restaurants/protected/test")
                .header("Authorization", "Bearer good-token")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("protected-ok"));
    }

    @Test
    void protectedEndpointRejectsWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/restaurants/protected/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointRejectsInvalidToken() throws Exception {
        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

        mockMvc.perform(post("/api/restaurants/protected/test")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRequestsArePublic() throws Exception {
        mockMvc.perform(get("/api/restaurants/public/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("public-ok"));
    }

    @RestController
    public static class TestController {
        @PostMapping("/api/restaurants/onboard")
        public String onboard() {
            return "onboard-ok";
        }

        @PostMapping("/api/restaurants/login")
        public String login() {
            return "login-ok";
        }

        @PostMapping("/api/restaurants/protected/test")
        public String secured() {
            return "protected-ok";
        }

        @GetMapping("/api/restaurants/public/test")
        public String publicEndpoint() {
            return "public-ok";
        }
    }
}
