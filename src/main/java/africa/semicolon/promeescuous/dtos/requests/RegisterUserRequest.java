package africa.semicolon.promeescuous.dtos.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {
    private String email;
    private String password;
}
