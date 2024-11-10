package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponentImpl;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.VerifyEmailDTO;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.Outputs.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false)
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
        String location = mockMvc.perform(post("/api/v1/users/signup")
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
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCode = extractSentCode(mimeMessage);

        // verify-email
        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCode)
                .build();

        mockMvc.perform(post("/api/v1/users/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());

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

    private String extractSentCode(MimeMessage mimeMessage) throws MessagingException {
        AtomicReference<String> verificationToken = new AtomicReference<>();

        verify(javaMailSender).send(any(MimeMessage.class));
        verify(mimeMessage).setContent(argThat(multipart -> {
            try {
                verificationToken.set(getToken(multipart));
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));

        return verificationToken.get();
    }

    /*
       html ..........
       <p class="code">12345689...</p>
       html ..........
        */
    private String getToken(Multipart multipart) throws MessagingException, IOException {
        String content = getContent(multipart);

        // <p class="code">value-here</p>
        Pattern pattern = Pattern.compile("<p class=\"code\">(.*?)</p>");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);  // return the code value
        }

        throw new IllegalArgumentException("Token not found in the email content");
    }

    private String getContent(Multipart multipart) throws MessagingException, IOException {
        Path file = Files.createTempFile("cris6h16", ".txt");
        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.WRITE)) {
            multipart.writeTo(os);
        }
        byte[] fileBytes = Files.readAllBytes(file);
        String content = new String(fileBytes, StandardCharsets.UTF_8);
        System.out.println("Content: " + content);

        return content;
    }

    @Test
    void verifyEmail_successful() throws Exception {
        // Arrange
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // create
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCode = extractSentCode(mimeMessage);

        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCode)
                .build();

        // Act
        mockMvc.perform(post("/api/v1/users/verify-email")
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
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        String sentCodeWhenSignup = extractSentCode(mimeMessage);

        // verify-email
        VerifyEmailDTO verifyEmailDTO = VerifyEmailDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCodeWhenSignup)
                .build();

        mockMvc.perform(post("/api/v1/users/verify-email")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyEmailDTO)))
                .andExpect(status().isOk());

        // request a new email verification code ( code can be used only once )
        Mockito.clearInvocations(mimeMessage, javaMailSender);

        mockMvc.perform(post("/api/v1/email/send-email-verification")
                        .contentType(TEXT_PLAIN)
                        .content(this.dto.getEmail()))
                .andExpect(status().isOk());

        String sentCodeWhenRequestedACode = extractSentCode(mimeMessage);

        // reset-password
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder()
                .email(this.dto.getEmail())
                .code(sentCodeWhenRequestedACode)
                .password("87654321")
                .build();
        // Act
        mockMvc.perform(post("/api/v1/users/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordDTO)))
                .andExpect(status().isNoContent());


        // Assert
        // login with the new password
        LoginDTO loginDTO = LoginDTO.builder()
                .email(this.dto.getEmail())
                .password(resetPasswordDTO.getPassword())
                .build();

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

}