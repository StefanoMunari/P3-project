package server;

public class InvalidArgumentException extends Exception{
	private final String message;

	public InvalidArgumentException(final String message)
	{
		this.message= message;
	}
	public String getMessage(){
		return message;
	}
}