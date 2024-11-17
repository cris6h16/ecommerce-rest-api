package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.user.LoginOutput;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Common {

    public static String extractSentCode(MimeMessage mimeMessage, JavaMailSender javaMailSender) throws MessagingException {
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
   public static String getToken(Multipart multipart) throws MessagingException, IOException {
        String content = getContent(multipart);

        // <p class="code">value-here</p>
        Pattern pattern = Pattern.compile("<p class=\"code\">(.*?)</p>");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);  // return the code value
        }

        throw new IllegalArgumentException("Token not found in the email content");
    }

 public    static String getContent(Multipart multipart) throws MessagingException, IOException {
        Path file = Files.createTempFile("cris6h16", ".txt");
        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.WRITE)) {
            multipart.writeTo(os);
        }
        byte[] fileBytes = Files.readAllBytes(file);
        String content = new String(fileBytes, StandardCharsets.UTF_8);
        System.out.println("Content: " + content);

        return content;
    }




    public static LoginOutput login(SignupDTO dto, MockMvc mockMvc) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDTO loginDTO = LoginDTO.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, LoginOutput.class);
    }

}
