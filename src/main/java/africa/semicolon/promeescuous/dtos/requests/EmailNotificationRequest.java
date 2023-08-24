package africa.semicolon.promeescuous.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmailNotificationRequest {
    @JsonProperty("to")
    private List<Recipient> to;
    private Sender sender;
    @JsonProperty("cc")
    private List<String> copiedEmails;
    @JsonProperty("htmlContent")
    private String mailContent;
    private String textContent;
    private String subject;
}
