package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.Main;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.VerifyEmailDTO;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserOutput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.extractSentCode;
import static org.mockito.Mockito.when;
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
    private JavaMailSender javaMailSender; // avoid sending real emails

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CreateUserInput dto = CreateUserInput.builder()
            .firstname("Cristian")
            .lastname("Herrera")
            .email("cristianmherrera21@gmail.com")
            .password("12345678")
            .build();


    // 0.............. user ..............
    @Test
    void getUser_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCode = extractSentCode(mimeMessage, javaMailSender);

        // verify-email
        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCode)
                .build();

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());

        LoginDTO dto = LoginDTO.builder()
                .email(this.dto.getEmail())
                .password(this.dto.getPassword())
                .build();

        // login
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginOutput output = objectMapper.readValue(response, LoginOutput.class);

        // Act
        String userStr = mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + output.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserOutput userOutput = objectMapper.readValue(userStr, UserOutput.class);
        assertThat(userOutput)
                .hasFieldOrPropertyWithValue("firstname", this.dto.getFirstname())
                .hasFieldOrPropertyWithValue("lastname", this.dto.getLastname())
                .hasFieldOrPropertyWithValue("email", this.dto.getEmail());
    }

}