package tp1.p2.control.exceptions;

public class InvalidPositionException extends GameException {

	/*
	 * lanzada cuando una posición del juego proporcionada por 
	 * el usuario está ocupada o no pertenece a una casilla 
	 * válida. Debería almacenar la posición (col, row)
	 */
	
	public InvalidPositionException() {
		super(); 
	}
	
	public InvalidPositionException(String message) {
		super(message);
	}
	
}
