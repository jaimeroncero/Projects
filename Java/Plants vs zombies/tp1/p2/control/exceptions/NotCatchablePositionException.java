package tp1.p2.control.exceptions;

public class NotCatchablePositionException extends GameException {
	
	public NotCatchablePositionException() {
		super(); 
	}
	
	public NotCatchablePositionException(String message) {
		super(message);
	}
}
