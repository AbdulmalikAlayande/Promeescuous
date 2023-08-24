package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddressUpdateResponse{
	private String street;
	private String houseNumber;
	private String state;
	private String country;
	private String message;
}
