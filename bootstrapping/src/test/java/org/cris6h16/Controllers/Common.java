package org.cris6h16.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    public static SignupDTO defSignupDTO = SignupDTO.builder()
            .firstname("cris6h16")
            .lastname("inGithub")
            .email("c@gmail.com")
            .password("12345678")
            .build();

    public static void removeAll(
            UserComponent userComponent,
            EmailComponent emailComponent,
            ProductComponent productComponent,
            TransactionTemplate transactionTemplate) {
        transactionTemplate.execute(action -> {
            userComponent.deleteAll();
            emailComponent.deleteAll();
            productComponent.deleteAll();
            return null;
        });
    }

    public static LoginOutput loginOutputInExpectedState(
            UserComponent userComponent,
            SecurityComponent securityComponent,
            TransactionTemplate transactionTemplate,
            String authority
    ) {
        Set<String> authorities = new HashSet<>(List.of(authority));
        AtomicReference<Long> id = new AtomicReference<>(null);
        transactionTemplate.executeWithoutResult(status -> {
            id.set(userComponent.create(CreateUserInput.builder()
                    .firstname(defSignupDTO.getFirstname())
                    .lastname(defSignupDTO.getLastname())
                    .email(defSignupDTO.getEmail())
                    .emailVerified(true)
                    .authorities(authorities)
                    .password(securityComponent.encodePassword(defSignupDTO.getPassword()))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .build()));
        });

        return new LoginOutput(
                securityComponent.generateAccessToken(id.get(), authorities),
                securityComponent.generateRefreshToken(id.get(), authorities)
        );
    }



    static LoginDTO loginDTOInExpectedState(UserComponent userComponent, SecurityComponent securityComponent, TransactionTemplate transactionTemplate) {
        transactionTemplate.executeWithoutResult(status -> {
            userComponent.create(CreateUserInput.builder()
                    .firstname(defSignupDTO.getFirstname())
                    .lastname(defSignupDTO.getLastname())
                    .email(defSignupDTO.getEmail())
                    .emailVerified(true)
                    .authorities(new HashSet<>(List.of("ROLE_USER")))
                    .password(securityComponent.encodePassword(defSignupDTO.getPassword()))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .build());
        });

        return LoginDTO.builder()
                .email(defSignupDTO.getEmail())
                .password(defSignupDTO.getPassword())
                .build();
    }


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

    public static String getContent(Multipart multipart) throws MessagingException, IOException {
        Path file = Files.createTempFile("cris6h16", ".txt");
        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.WRITE)) {
            multipart.writeTo(os);
        }
        byte[] fileBytes = Files.readAllBytes(file);
        String content = new String(fileBytes, StandardCharsets.UTF_8);
        System.out.println("Content: " + content);

        return content;
    }



}
