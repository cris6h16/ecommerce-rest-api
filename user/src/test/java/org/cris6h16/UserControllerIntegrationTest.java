package org.cris6h16;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = UserControllerIntegrationTest.App.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SignupDTO dto = SignupDTO.builder()
            .firstname("Cristian")
            .lastname("Herrera")
            .email("cristianmherrera21@gmail.com")
            .password("12345678")
            .build();

    @Test
    void signup_successful() throws Exception {
        // Arrange

        // Act
        String location = mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // Assert
        assertNotNull(location);
        assertTrue(location.matches("/api/v1/users/\\d+"));
        // todo: mock.perform( get(location) ).andExpect( status().isOk() );
    }

    @Test
    void login_successful() throws Exception {
        // Arrange
        mockMvc.perform(post("/api/v1/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        LoginDTO dto = LoginDTO.builder()
                .email(this.dto.getEmail())
                .password(this.dto.getPassword())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        LoginOutput output = objectMapper.readValue(response, LoginOutput.class);
        assertNotNull(output.getAccessToken());
        assertNotNull(output.getRefreshToken());
    }


    @SpringBootApplication
    static class App {

        public static void main(String[] args) {
            SpringApplication.run(App.class, args);
        }

    }

}