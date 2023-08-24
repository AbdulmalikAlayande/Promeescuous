package africa.semicolon.promeescuous.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAddressResponse {
	private Long id;
	private String street;
	private String houseNumber;
	private String state;
	private String country;
	private String message;
}
