package africa.semicolon.promeescuous.exceptions;

public class PromiscuousBaseException extends RuntimeException{
	
	public PromiscuousBaseException(String message){
		super(message);
	}
	
	public PromiscuousBaseException(Throwable throwable){
		super(throwable);
	}
}
