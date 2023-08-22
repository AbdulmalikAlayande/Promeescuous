package africa.semicolon.promeescuous.dtos.responses;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class ActivateAccountResponse {
	
	private String message;
	private GetUserResponse user;
}
