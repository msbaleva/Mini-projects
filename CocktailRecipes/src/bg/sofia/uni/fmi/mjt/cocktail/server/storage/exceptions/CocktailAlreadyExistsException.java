package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions;

public class CocktailAlreadyExistsException extends RuntimeException {

    public CocktailAlreadyExistsException(String message) {
        super(message);
    }

    public CocktailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
