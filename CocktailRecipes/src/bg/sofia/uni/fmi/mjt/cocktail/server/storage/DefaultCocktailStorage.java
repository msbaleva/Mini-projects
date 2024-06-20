package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultCocktailStorage implements CocktailStorage {

    private Map<String, Cocktail> cocktails = new HashMap<>();
    private Map<String, Set<String>> cocktailNamesWithIngredient = new HashMap<>();
    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        String cocktailName = cocktail.name();
        if (cocktails.containsKey(cocktailName)) {
            throw new CocktailAlreadyExistsException(String.format("Cocktail %s already exists.", cocktailName));
        }

        cocktails.put(cocktailName, cocktail);
        for (Ingredient i : cocktail.ingredients()) {
            cocktailNamesWithIngredient.putIfAbsent(i.name(), new HashSet<>());
            cocktailNamesWithIngredient.get(i.name()).add(cocktail.name());
        }
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktails.values();
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        return cocktailNamesWithIngredient.get(ingredientName).stream()
                .map(name -> cocktails.get(name))
                .collect(Collectors.toList());
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        Cocktail cocktail = cocktails.get(name);
        if (cocktail == null) {
            throw new CocktailNotFoundException(String.format("Cocktail %s not founds", name));
        }

        return cocktail;
    }

}
