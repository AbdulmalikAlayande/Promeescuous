package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
	private String accessToken;
}
