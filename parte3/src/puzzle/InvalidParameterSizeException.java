package puzzle;

public class InvalidParameterSizeException extends Exception {
/****FIELDS****/
	private final String message;//non modificabile una volta settato dal costruttore
/****METHODS****/
	public InvalidParameterSizeException(final String e){
		message=e;
	}
	public String getMessage(){
		return message;
	}
}
