package africa.semicolon.promeescuous.dtos.requests;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressCreationRequest {
	private String houseNumber;
	private String street;
	private String state;
	private String country;
}
