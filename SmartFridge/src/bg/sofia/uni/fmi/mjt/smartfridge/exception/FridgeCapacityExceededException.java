package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class FridgeCapacityExceededException extends RuntimeException {

    public FridgeCapacityExceededException(String message) {
        super(message);
    }

    public FridgeCapacityExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
