package tp1.p2.control.exceptions;

public class RecordException extends GameException {

	/*
	 * lanzada cuando hay problemas en la lectura o 
	 * escritura del récord
	 */
	public RecordException() {
		super(); 
	}
	
	public RecordException(String message) {
		super(message);
	}

	public RecordException(String message, Throwable cause) {
		super(message, cause);
	}
}
