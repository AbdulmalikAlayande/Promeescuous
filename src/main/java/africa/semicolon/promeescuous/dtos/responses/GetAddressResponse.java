package africa.semicolon.promeescuous.dtos.responses;

import lombok.Builder;

@Builder
public record GetAddressResponse(
		String street,
		String houseNumber,
		String state,
		String country,
		String message
) {
}
