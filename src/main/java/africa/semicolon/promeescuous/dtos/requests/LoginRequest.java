package africa.semicolon.promeescuous.dtos.requests;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	private String password;
	private String email;
	
}
