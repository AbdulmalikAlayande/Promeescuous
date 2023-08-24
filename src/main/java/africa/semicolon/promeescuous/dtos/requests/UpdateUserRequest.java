package africa.semicolon.promeescuous.dtos.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUserRequest {
	
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String password;
	private String gender;
	private Set<String> interests;
	private MultipartFile profileImage;
	private String phoneNumber;
	private String houseNumber;
	private String street;
	private String state;
	private String country;
	
}
