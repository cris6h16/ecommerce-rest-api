package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.facades.UserDTO;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.defSignupDTO;
import static org.cris6h16.Controllers.Common.loginDTOInExpectedState;
import static org.cris6h16.Controllers.Common.loginOutputInExpectedState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    UserComponent userComponent;

    @Autowired
    SecurityComponent securityComponent;

    @BeforeEach
    void beforeEach(
            @Autowired ProductComponent productComponent,
            @Autowired EmailComponent emailComponent,
            @Autowired TransactionTemplate transactionTemplate) {
        Common.removeAll(
                userComponent,
                emailComponent,
                productComponent,
                transactionTemplate
        );
    }

    // 0.............. user ..............
    @Test
    void getUser_successful() throws Exception {
        // Arrange
        LoginOutput output = loginOutputInExpectedState(userComponent, securityComponent, transactionTemplate, "ROLE_USER");

        // Act
        String userStr = mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + output.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        UserDTO userDTO = objectMapper.readValue(userStr, UserDTO.class);
        assertTrue(userDTO.getId() > 0);
        assertEquals(0, userDTO.getBalance().compareTo(BigDecimal.ZERO));
        assertThat(userDTO)
                .hasFieldOrPropertyWithValue("firstname", defSignupDTO.getFirstname())
                .hasFieldOrPropertyWithValue("lastname", defSignupDTO.getLastname())
                .hasFieldOrPropertyWithValue("email", defSignupDTO.getEmail())
                .hasFieldOrPropertyWithValue("authorities", new HashSet<>(List.of("ROLE_USER")))
                .hasFieldOrPropertyWithValue("enabled", true)
                .hasFieldOrPropertyWithValue("emailVerified", true);
    }

}