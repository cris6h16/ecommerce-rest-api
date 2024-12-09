package org.cris6h16.Controllers;

import jakarta.mail.internet.MimeMessage;
import org.cris6h16.Main;
import org.cris6h16.facades.SignupDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Main.class)
@ActiveProfiles("test")
class AuthenticationFunctionalTest {

    @MockBean
    private JavaMailSender javaMailSender; // mock

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    void signup_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        SignupDTO dto = SignupDTO.builder()
                .firstname("Salomon")
                .lastname("Quilumba")
                .email("salomon@gmail.com")
                .password("salomon1")
                .build();

        // Act
        ResponseEntity<Void> res = restTemplate
                .postForEntity("/api/v1/auth/signup", dto, Void.class);

        // Assert
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = res.getHeaders().getLocation().getPath();
        assertTrue(location.matches("/api/v1/users/\\d+"));

        String code = Common.extractSentCode(mimeMessage, javaMailSender);
        assertTrue(code.matches("^[a-zA-Z0-9]+$"));
    }
//
//    private void afterSignupExpectedState(SignupDTO dto) {
//        UserDTO output = userComponent.findByEmailAndEnabled(dto.getEmail(), true).orElseThrow();
//        assertEquals(0, output.getBalance().compareTo(BigDecimal.ZERO));
//        assertTrue(output.getId() > 0);
//        assertTrue(output.getPassword().startsWith("{bcrypt}"));
//        assertThat(output)
//                .hasFieldOrPropertyWithValue("firstname", dto.getFirstname())
//                .hasFieldOrPropertyWithValue("lastname", dto.getLastname())
//                .hasFieldOrPropertyWithValue("email", dto.getEmail())
//                .hasFieldOrPropertyWithValue("authorities", new HashSet<>(List.of("ROLE_USER")))
//                .hasFieldOrPropertyWithValue("enabled", true)
//                .hasFieldOrPropertyWithValue("emailVerified", false);
//    }
//
//    @Test
//    void login_successful() throws Exception {
//        // Arrange
//        LoginDTO loginDTO = loginDTOInExpectedState(userComponent, securityComponent, transactionTemplate);
//
//        // Act
//        String response = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDTO)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        // Assert
//        LoginOutput output = objectMapper.readValue(response, LoginOutput.class);
//        assertNotNull(output.getAccessToken());
//        assertNotNull(output.getRefreshToken());
//        assertTrue(securityComponent.isTokenValid(output.getAccessToken()));
//        assertTrue(securityComponent.isTokenValid(output.getRefreshToken()));
//    }
//
//
//    @Test
//    void verifyEmail_successful() throws Exception {
//        // Arrange
//        VerifyEmailDTO verifyEmailDTO = verifyEmailDTOInExpectedState();
//
//        // Act
//        mockMvc.perform(post("/api/v1/auth/verify-email")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
//                .andExpect(status().isOk());
//
//        // Assert
//        hasEmailVerified(verifyEmailDTO.getEmail());
//    }
//
//    private void hasEmailVerified(String email) {
//        UserDTO output = userComponent.findByEmailAndEnabled(email, true).orElseThrow();
//        assertTrue(output.isEmailVerified());
//    }
//
//    private VerifyEmailDTO verifyEmailDTOInExpectedState() {
//        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
//
//        String email = "c@gmail.com";
//        AtomicReference<String> code = new AtomicReference<>(null);
//        transactionTemplate.executeWithoutResult(status -> {
//            userComponent.create(CreateUserInput.builder()
//                    .firstname("cris6h16")
//                    .lastname("inGithub")
//                    .email(email)
//                    .emailVerified(false)
//                    .authorities(new HashSet<>(List.of("ROLE_USER")))
//                    .password(securityComponent.encodePassword("12345678"))
//                    .balance(BigDecimal.ZERO)
//                    .enabled(true)
//                    .build());
//            code.set(emailComponent.sendEmailVerificationCode(email));
//        });
//        return VerifyEmailDTO.builder()
//                .email(email)
//                .code(code.get())
//                .build();
//    }
//
//    @Test
//    void resetPassword_successful() throws Exception {
//        // Arrange
//        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTOInExpectedState();
//
//        // Act
//        mockMvc.perform(post("/api/v1/auth/reset-password")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(resetPasswordDTO)))
//                .andExpect(status().isNoContent());
//
//        // Assert
//        UserDTO output = userComponent.findByEmailAndEnabled(resetPasswordDTO.getEmail(), true).orElseThrow();
//        assertTrue(securityComponent.matches(resetPasswordDTO.getPassword(), output.getPassword()));
//    }
//
//    private ResetPasswordDTO ResetPasswordDTOInExpectedState() {
//        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
//
//        String email = "c@gmail.com";
//        AtomicReference<String> code = new AtomicReference<>(null);
//        transactionTemplate.executeWithoutResult(status -> {
//            userComponent.create(CreateUserInput.builder()
//                    .firstname("cris6h16")
//                    .lastname("inGithub")
//                    .email(email)
//                    .emailVerified(true)
//                    .authorities(new HashSet<>(List.of("ROLE_USER")))
//                    .password(securityComponent.encodePassword("12345678"))
//                    .balance(BigDecimal.ZERO)
//                    .enabled(true)
//                    .build());
//            code.set(emailComponent.sendEmailVerificationCode(email));
//        });
//
//        return ResetPasswordDTO.builder()
//                .email(email)
//                .code(code.get())
//                .password("newPassword")
//                .build();
//    }
//
//    @Test
//    void refreshAccessToken_successful() throws Exception {
//        // Arrange
//        LoginOutput output = loginOutputInExpectedState(userComponent, securityComponent, transactionTemplate, "ROLE_USER");
//
//        // Act
//        String accessToken = mockMvc.perform(post("/api/v1/auth/refresh-token")
//                        .contentType(APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + output.getRefreshToken()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        // Assert
//        assertThat(accessToken).isNotNull();
//        assertTrue(securityComponent.isTokenValid(accessToken));
//    }
//
//    @Test
//    void sendEmailVerification_successful() throws Exception {
//        // Arrange
//        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
//        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//        LoginDTO loginDTO = loginDTOInExpectedState(userComponent, securityComponent, transactionTemplate);
//
//        // Act
//        mockMvc.perform(post("/api/v1/auth/send-email-verification")
//                        .contentType(TEXT_PLAIN)
//                        .content(loginDTO.getEmail()))
//                .andExpect(status().isOk());
//
//        // Assert
//        AtomicReference<String> token = new AtomicReference<>(null);
//        verify(javaMailSender).send(any(MimeMessage.class));
//        verify(mimeMessage).setContent(argThat(multipart -> {
//            try {
//                token.set(getToken(multipart));
//            } catch (MessagingException | IOException e) {
//                throw new RuntimeException(e);
//            }
//            return true;
//        }));
//        assertTrue(emailComponent.isCodeValid(loginDTO.getEmail(), token.get()));
//    }
}