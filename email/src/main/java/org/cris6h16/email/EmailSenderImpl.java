package org.cris6h16.email;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.EmailComponentException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;
import java.util.Arrays;

import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_SENDING_MAX_RETRIES_ERROR;

@Slf4j
@Component
class EmailSenderImpl implements EmailSender {
    private static final int MAX_RETRIES = 3;
    private static final String APP_NAME = "Demo App";
    private final ITemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public EmailSenderImpl(ITemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @SuppressWarnings("SameParameterValue")
    private void sendEmail(String email, String content, String subject) {
        boolean success = false;

        log.debug("Trying to send an email to {}", email);
        log.trace("Email content: {}", content);

        for (int attempts = 0; attempts < MAX_RETRIES && !success; attempts++) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(content, true);

                log.trace("Sending email to {}, Content: {}, Subject: {}",
                        Arrays.toString(message.getAllRecipients()),
                        message.getContent(),
                        message.getSubject()
                );

                mailSender.send(message);

                log.debug("Email successfully sent to {} on attempt {}", email, attempts);
                return;

            } catch (Exception e) {
                if (attempts == MAX_RETRIES - 1) {
                    log.error("All attempts failed to send email to {}, {}", email, e.toString());
                } else {
                    log.warn("Attempt {}: Failed to send email to {}", attempts, email, e);
                }
            }
        }

        throw new EmailComponentException(EMAIL_SENDING_MAX_RETRIES_ERROR);
    }

    @Override
    public void sendEmailVerificationCode(String email, String verificationCode) {
        String content = verificationCodeContent(email, verificationCode);
        sendEmail(email, content, "Email Verification Code");
    }

    private String verificationCodeContent(String email, String verificationCode) {
        Context context = new Context();
        context.setVariable("correo", email);
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("appName", APP_NAME);
        context.setVariable("currentYear", Year.now().getValue());

        return templateEngine.process("email-verification-code.html", context);
    }
}
