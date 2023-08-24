package africa.semicolon.promeescuous.dtos.responses;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateAccountResponse {
	
	private String message;
	private GetUserResponse user;
}
