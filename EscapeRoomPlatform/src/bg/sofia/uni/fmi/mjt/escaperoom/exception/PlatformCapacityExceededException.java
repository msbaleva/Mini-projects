package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class PlatformCapacityExceededException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 629591430283907327L;
	
	public PlatformCapacityExceededException(String message) {
		super(message);
	}
	
	public PlatformCapacityExceededException(String message, Throwable cause) {
		super(message, cause);
	}

}
