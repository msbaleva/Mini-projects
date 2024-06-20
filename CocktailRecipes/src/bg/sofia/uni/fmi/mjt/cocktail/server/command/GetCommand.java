package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

public class GetCommand implements Command {

    private static final String REPLY = "{\"status\":\"OK\",\"cocktails\": %s}";
    private static final String ALL_COMMAND = "all";
    private static final String BY_NAME_COMMAND = "by-name";
    private static final String BY_INGREDIENT_COMMAND = "by-ingredient";
    private static final String EMPTY = "[]";
    private String cocktails;

    GetCommand(String[] query) {
        this.cocktails = initCocktails(query);
    }

    String initCocktails(String[] query) {
        switch (query[1]) {
            case ALL_COMMAND -> {
                return storage.getCocktails().toString();
            }
            case BY_NAME_COMMAND -> {
                try {
                    return storage.getCocktail(query[2]).toString();
                } catch (CocktailNotFoundException e) {
                    return EMPTY;
                }
            }
            case BY_INGREDIENT_COMMAND -> {
                return storage.getCocktailsWithIngredient(query[2]).toString();
            }
        }

        return EMPTY;
    }

    @Override
    public String execute() {
        return String.format(REPLY, cocktails);
    }
}
