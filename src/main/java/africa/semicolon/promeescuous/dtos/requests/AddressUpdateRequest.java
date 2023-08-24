package africa.semicolon.promeescuous.dtos.requests;

import lombok.*;

@Builder
public record AddressUpdateRequest(
		Long id,
		String street,
		String houseNumber,
		String state,
		String country
) {

}
