package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598312342673411509L;

	public RoomAlreadyExistsException(String message) {
		super(message);
	}
	
	public RoomAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
