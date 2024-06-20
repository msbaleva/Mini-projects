package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5442730174619585724L;
	
	public RoomNotFoundException(String message) {
		super(message);
	}
	
	public RoomNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
