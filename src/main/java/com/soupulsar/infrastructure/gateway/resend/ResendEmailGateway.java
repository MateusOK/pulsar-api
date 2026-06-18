package com.soupulsar.infrastructure.gateway.resend;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import com.soupulsar.application.interfaces.EmailGateway;
import com.soupulsar.infrastructure.exceptions.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResendEmailGateway implements EmailGateway {

    private final Resend resend;

    @Value("${resend.fromEmail}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String resetLink, String name, String email) {

        Template template = getTemplate(resetLink, name);

        CreateEmailOptions options = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(email)
                .template(template)
                .build();
        try {
            resend.emails().send(options);
            log.info("Password reset email sent to {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}", email, e);
            throw new EmailDeliveryException("Failed to send password reset email", e);
        }
    }

    private Template getTemplate(String resetLink, String name) {
        return Template.builder()
                .id("password-reset")
                .addVariable("reset_link", resetLink)
                .addVariable("first_name", name)
                .build();
    }
}