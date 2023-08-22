package africa.semicolon.promeescuous.exceptions;

import lombok.RequiredArgsConstructor;

public class UserNotFoundException extends PromiscuousBaseException{
	
	public UserNotFoundException(String message){
		super(message);
	}
	
}
