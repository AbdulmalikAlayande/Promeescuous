package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;
import africa.semicolon.promeescuous.models.User;

import java.io.IOException;
import java.net.URISyntaxException;

public interface MailService {
    EmailNotificationResponse send(EmailNotificationRequest emailNotificationRequest);
	
	String send(User user) throws URISyntaxException, IOException;
}
