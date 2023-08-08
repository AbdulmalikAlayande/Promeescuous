package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.requests.Recipient;
import africa.semicolon.promeescuous.dtos.requests.Sender;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class MailServiceTest {
    @Autowired
    private MailService mailService;
    private EmailNotificationRequest request;

    @BeforeEach
    public void setUp(){
        String recipientEmail = "alaabdulmalik03@gmail.com";
        String message = "<p>testing our mail service</p>";
        String mailSender = "noreply@promiscuous.com";
        String subject = "test email";

        request = new EmailNotificationRequest();
        request.setMailContent(message);
        request.setRecipients(List.of(new Recipient(recipientEmail)));
        request.setSubject(subject);
        request.setSender(new Sender(mailSender));
    }
    @Test
    public void testThatEmailSendingWorks(){
        EmailNotificationResponse emailNotificationResponse = mailService.send(request);
        log.info("response:: {}", emailNotificationResponse);
        assertNotNull(emailNotificationResponse);
    }
}
