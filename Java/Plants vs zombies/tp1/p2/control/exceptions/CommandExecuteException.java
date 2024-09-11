package tp1.p2.control.exceptions;

public class CommandExecuteException extends GameException{

	/*
	 * Lanzada por algún error detectado en la ejecución de un 
	 * comando y que no tenga un subtipo de excepción más específico.
	 */
	
	
	public CommandExecuteException() {
		super(); 
	}

	public CommandExecuteException(String message) {
		super(message);
	}
	
}
