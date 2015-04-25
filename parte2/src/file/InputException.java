package file;

public class InputException extends Exception {
/****FIELDS****/
	private final String message;//non modificabile una volta settato dal costruttore
/****METHODS****/
	public InputException(final String e){
		message= e;
	}
	public String getMessage(){
		return message;
	}
}
