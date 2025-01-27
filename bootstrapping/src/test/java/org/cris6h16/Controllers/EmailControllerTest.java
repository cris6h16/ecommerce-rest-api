package org.cris6h16.Controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.cris6h16.facades.EmailFacade;
import org.cris6h16.facades.SendEmailVerificationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmailController.class)
class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailFacade emailFacade;

    @Test
    void sendEmailVerification_shouldReturnNoContent() throws Exception {
        // Arrange
        SendEmailVerificationDTO dto = new SendEmailVerificationDTO("test@example.com");
        doNothing().when(emailFacade).sendEmailVerificationCodeIfExists(any(SendEmailVerificationDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/email/send-email-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\"}"))
                .andExpect(status().isNoContent());

        // Verify that the facade method was called
        verify(emailFacade, times(1)).sendEmailVerificationCodeIfExists(any(SendEmailVerificationDTO.class));
    }
}
