package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressCreationResponse {
	private Long id;
	private String houseNumber;
	private String street;
	private String state;
	private String country;
}
