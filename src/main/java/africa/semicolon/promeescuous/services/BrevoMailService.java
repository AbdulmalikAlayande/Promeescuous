package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.config.AppConfig;
import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.requests.Recipient;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;
import africa.semicolon.promeescuous.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BrevoMailService implements MailService{

    private final AppConfig appConfig;
    @Override
    public EmailNotificationResponse send(EmailNotificationRequest emailNotificationRequest) {
        String brevoMailAddress = "https://api.brevo.com/v3/smtp/email";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", appConfig.getMailApiKey());
        headers.set("Content-Type", "application/json");
        HttpEntity<EmailNotificationRequest> request =
                new HttpEntity<>(emailNotificationRequest, headers);

        ResponseEntity<EmailNotificationResponse> response = restTemplate.postForEntity(brevoMailAddress, request, EmailNotificationResponse.class);
	    return response.getBody();
    }
    
    @Override
    public String send(User user) {
        EmailNotificationRequest notificationRequest = new EmailNotificationRequest();
        notificationRequest.setMailContent("<p>Hello Sending With Brevo<p>");
        Recipient recipient = new Recipient(user.getEmail());
        notificationRequest.setRecipients(List.of(recipient));
        EmailNotificationResponse response = send(notificationRequest);
        return response.getMessageId();
    }
}
