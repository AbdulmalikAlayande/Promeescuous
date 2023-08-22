package africa.semicolon.promeescuous.dtos.responses;

import lombok.Getter;

@Getter
public enum ResponseMessages {
	
	ACCOUNT_ACTIVATION_SUCCESSFUL(""),
	USER_REGISTRATION_SUCCESSFUL("");
	private final String name;
	
	ResponseMessages(String name) {
		this.name = name ;
	}
}
