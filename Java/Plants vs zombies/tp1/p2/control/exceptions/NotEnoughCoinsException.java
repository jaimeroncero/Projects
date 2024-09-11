package tp1.p2.control.exceptions;

public class NotEnoughCoinsException extends GameException {

	/*
	 * lanzada cuando no es posible realizar alguna acción 
	 * pedida por el usuario al no haber suficientes
	 * suncoins para llevarla a cabo
	 */
	public NotEnoughCoinsException() {
		super(); 
	}
	
	public NotEnoughCoinsException(String message) {
		super(message);
	}
}
