package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.EmailNotificationRequest;
import africa.semicolon.promeescuous.dtos.responses.EmailNotificationResponse;
import africa.semicolon.promeescuous.models.User;

public interface MailService {
    EmailNotificationResponse send(EmailNotificationRequest emailNotificationRequest);
	
	String send(User user);
}
