package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.facades.VerifyEmailDTO;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.UserComponent;
import org.cris6h16.user.UserDTO;
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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.getToken;
import static org.cris6h16.Controllers.Common.loginDTOInExpectedState;
import static org.cris6h16.Controllers.Common.loginOutputInExpectedState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
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

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private SecurityComponent securityComponent;
    @Autowired
    private EmailComponent emailComponent;

    @BeforeEach
    void beforeEach(
            @Autowired ProductComponent productComponent,
            @Autowired TransactionTemplate transactionTemplate) {
        Common.removeAll(
                userComponent,
                emailComponent,
                productComponent,
                transactionTemplate
        );
    }

    @Test
    void signup_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        SignupDTO dto = SignupDTO.builder()
                .firstname("cris6h16")
                .lastname("inGithub")
                .email("abc@gmail.com")
                .password("12345678")
                .build();

        // Act
        String location = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // Assert
        assertTrue(location.matches("/api/v1/users/me"));
        String code = Common.extractSentCode(mimeMessage, javaMailSender);
        assertTrue(emailComponent.isCodeValid(dto.getEmail(), code));
        afterSignupExpectedState(dto);
    }

    private void afterSignupExpectedState(SignupDTO dto) {
        UserDTO output = userComponent.findByEmailAndEnabled(dto.getEmail(), true).orElseThrow();
        assertEquals(0, output.getBalance().compareTo(BigDecimal.ZERO));
        assertTrue(output.getId() > 0);
        assertTrue(output.getPassword().startsWith("{bcrypt}"));
        assertThat(output)
                .hasFieldOrPropertyWithValue("firstname", dto.getFirstname())
                .hasFieldOrPropertyWithValue("lastname", dto.getLastname())
                .hasFieldOrPropertyWithValue("email", dto.getEmail())
                .hasFieldOrPropertyWithValue("authorities", new HashSet<>(List.of("ROLE_USER")))
                .hasFieldOrPropertyWithValue("enabled", true)
                .hasFieldOrPropertyWithValue("emailVerified", false);
    }

    @Test
    void login_successful() throws Exception {
        // Arrange
        LoginDTO loginDTO = loginDTOInExpectedState(userComponent, securityComponent, transactionTemplate);

        // Act
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        LoginOutput output = objectMapper.readValue(response, LoginOutput.class);
        assertNotNull(output.getAccessToken());
        assertNotNull(output.getRefreshToken());
        assertTrue(securityComponent.isTokenValid(output.getAccessToken()));
        assertTrue(securityComponent.isTokenValid(output.getRefreshToken()));
    }



    @Test
    void verifyEmail_successful() throws Exception {
        // Arrange
        VerifyEmailDTO verifyEmailDTO = verifyEmailDTOInExpectedState();

        // Act
        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());

        // Assert
        hasEmailVerified(verifyEmailDTO.getEmail());
    }

    private void hasEmailVerified(String email) {
        UserDTO output = userComponent.findByEmailAndEnabled(email, true).orElseThrow();
        assertTrue(output.isEmailVerified());
    }

    private VerifyEmailDTO verifyEmailDTOInExpectedState() {
        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));

        String email = "c@gmail.com";
        AtomicReference<String> code = new AtomicReference<>(null);
        transactionTemplate.executeWithoutResult(status -> {
            userComponent.create(CreateUserInput.builder()
                    .firstname("cris6h16")
                    .lastname("inGithub")
                    .email(email)
                    .emailVerified(false)
                    .authorities(new HashSet<>(List.of("ROLE_USER")))
                    .password(securityComponent.encodePassword("12345678"))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .build());
            code.set(emailComponent.sendEmailVerificationCode(email));
        });
        return VerifyEmailDTO.builder()
                .email(email)
                .code(code.get())
                .build();
    }

    @Test
    void resetPassword_successful() throws Exception {
        // Arrange
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTOInExpectedState();

        // Act
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordDTO)))
                .andExpect(status().isNoContent());

        // Assert
        UserDTO output = userComponent.findByEmailAndEnabled(resetPasswordDTO.getEmail(), true).orElseThrow();
        assertTrue(securityComponent.matches(resetPasswordDTO.getPassword(), output.getPassword()));
    }

    private ResetPasswordDTO ResetPasswordDTOInExpectedState() {
        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));

        String email = "c@gmail.com";
        AtomicReference<String> code = new AtomicReference<>(null);
        transactionTemplate.executeWithoutResult(status -> {
            userComponent.create(CreateUserInput.builder()
                    .firstname("cris6h16")
                    .lastname("inGithub")
                    .email(email)
                    .emailVerified(true)
                    .authorities(new HashSet<>(List.of("ROLE_USER")))
                    .password(securityComponent.encodePassword("12345678"))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .build());
            code.set(emailComponent.sendEmailVerificationCode(email));
        });

        return ResetPasswordDTO.builder()
                .email(email)
                .code(code.get())
                .password("newPassword")
                .build();
    }

    @Test
    void refreshAccessToken_successful() throws Exception {
        // Arrange
        LoginOutput output = loginOutputInExpectedState(userComponent, securityComponent, transactionTemplate, "ROLE_USER");

        // Act
        String accessToken = mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + output.getRefreshToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert
        assertThat(accessToken).isNotNull();
        assertTrue(securityComponent.isTokenValid(accessToken));
    }

    @Test
    void sendEmailVerification_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        LoginDTO loginDTO = loginDTOInExpectedState(userComponent, securityComponent, transactionTemplate);

        // Act
        mockMvc.perform(post("/api/v1/auth/send-email-verification")
                        .contentType(TEXT_PLAIN)
                        .content(loginDTO.getEmail()))
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
        assertTrue(emailComponent.isCodeValid(loginDTO.getEmail(), token.get()));
    }
}