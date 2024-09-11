package tp1.p2.control.exceptions;

public class GameException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Sirve para tratar las situaciones de error al ejecutar el método execute() 
	 * de un comando, por ejemplo, no tener suficientes suncoins para ejecutar un
	 * comando o que la casilla donde se quiere añadir un elemento esté ocupada
	 */

	public GameException() {
		super(); 
	}
	
	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}
}
