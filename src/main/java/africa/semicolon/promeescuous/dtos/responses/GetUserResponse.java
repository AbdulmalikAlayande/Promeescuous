package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Data
@RequiredArgsConstructor
@Builder
public class GetUserResponse {
	private Long id;
	private String email;
	private String fullName;
	private String address;
	private String phoneNumber;
	private String profileImage;
}
