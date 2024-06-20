package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;

public interface Command {

    DefaultCocktailStorage storage = new DefaultCocktailStorage();
    String execute();
}
