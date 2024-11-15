package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.Main;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.VerifyEmailDTO;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.extractSentCode;
import static org.cris6h16.Controllers.Common.getToken;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

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

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
        transactionTemplate.execute(status -> {
            userRepository.deleteAllInBatch();
            return null;
        });
    }

    @Test
    void signup_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        String location = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // Assert
        assertNotNull(location);
//        assertTrue(location.matches("/api/v1/users/\\d+"));
        assertTrue(location.matches("/api/v1/users/me"));
        // todo: mock.perform( get(location) ).andExpect( status().isOk() );
    }

    @Test
    void login_successful() throws Exception {
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

        // Act
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        LoginOutput output = objectMapper.readValue(response, LoginOutput.class);
        assertNotNull(output.getAccessToken());
        assertNotNull(output.getRefreshToken());
    }

    @Test
    void verifyEmail_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCode = extractSentCode(mimeMessage, javaMailSender);

        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCode)
                .build();

        // Act
        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCodeWhenSignup = extractSentCode(mimeMessage, javaMailSender);

        // verify-email
        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCodeWhenSignup)
                .build();

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());

        // request a new email verification code ( code can be used only once )
        Mockito.clearInvocations(mimeMessage, javaMailSender);

        mockMvc.perform(post("/api/v1/auth/send-email-verification")
                        .contentType(TEXT_PLAIN)
                        .content(this.dto.getEmail()))
                .andExpect(status().isOk());

        String sentCodeWhenRequestedACode = extractSentCode(mimeMessage, javaMailSender);

        // reset-password
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCodeWhenRequestedACode)
                .password("87654321")
                .build();

        // Act
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordDTO)))
                .andExpect(status().isNoContent());


        // Assert
        // login with the new password
        LoginDTO loginDTO = LoginDTO.builder()
                .email(this.dto.getEmail())
                .password(resetPasswordDTO.getPassword())
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void refreshAccessToken_successful() throws Exception {
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
        String accessToken = mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + output.getRefreshToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        assertThat(accessToken).isNotNull();
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void sendEmailVerification_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Mockito.clearInvocations(javaMailSender, mimeMessage);

        // Act
        mockMvc.perform(post("/api/v1/auth/send-email-verification")
                        .contentType(TEXT_PLAIN)
                        .content(this.dto.getEmail()))
                .andExpect(status().isOk());

        // Assert
        AtomicReference<String> token = new AtomicReference<>(null);
        verify(javaMailSender).send(any(MimeMessage.class));
        verify(mimeMessage).setContent(argThat(multipart -> {
            try {
                token.set(getToken(multipart));
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }));
        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(token.get())
                .build();
        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());
    }
}