package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;

public class CreateCommand implements Command {

    private static final String REPLY = "{\"status\":\"%s\"}";
    private static final String ERROR_REPLY = "{\"status\":\"%s\",\"errorMessage\":\"cocktail %s already exists\"}";
    private String[] query;

    public CreateCommand(String[] query) {
        this.query = query;
    }

    @Override
    public String execute() {
        String name = query[1];
        Cocktail cocktail = Cocktail.of(query);
        String replyFromServer = String.format(REPLY, Status.CREATED);
        try {
            storage.createCocktail(cocktail);
        } catch (CocktailAlreadyExistsException e) {
            replyFromServer = String.format(ERROR_REPLY, Status.ERROR, name);
        }

        return replyFromServer;
    }
}
