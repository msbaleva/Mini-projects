package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class TeamNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3440651829192376865L;
	
	public TeamNotFoundException(String message) {
		super(message);
	}
	
	public TeamNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
