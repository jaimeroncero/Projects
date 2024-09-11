package tp1.p2.control.exceptions;

public class CommandParseException extends GameException {

	/*
	 * Sirve para tratar los errores que ocurren durante la ejecucion del metodo parse(),
	 * como comando desconocido, numero de parametros incorrecto y tipo de parametros no valido
	 */
	
	/*
	 * NumberFormatException: excepcion lanzada cuando un elemento 
	 * proporcionado deberia ser un numero y no lo es
	 */
	
	public CommandParseException() {
		super(); 
	}
	
	public CommandParseException(String message) {
		super(message);
	}
	
	public CommandParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
