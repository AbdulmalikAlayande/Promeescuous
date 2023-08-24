package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Builder
public record AddressUpdateResponse(
		String street,
		String houseNumber,
		String state,
		String country,
		String message
) {

}
