package africa.semicolon.promeescuous.dtos.responses;

import lombok.Getter;

@Getter
public enum SuccessResponse {
	
	REGISTRATION_SUCCESSFUL("Registration Successful, check your email inbox for verification token"),
	UPDATE_SUCCESSFUL("");
	
	private final String name;
	SuccessResponse(String name) {
		this.name = name;
	}
}
