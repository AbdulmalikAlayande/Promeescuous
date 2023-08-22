package africa.semicolon.promeescuous.dtos.responses;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class LoginResponse {
	private String accessToken;
}
