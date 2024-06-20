import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Ingredient mint = new Ingredient("mint", "3gr");
        Ingredient gin = new Ingredient("gin", "100ml");
        Set<Ingredient> mochitoIngredients = new HashSet<>();
        mochitoIngredients.add(mint);
        mochitoIngredients.add(gin);
        Cocktail mochito = new Cocktail("Mochito", mochitoIngredients);
    }
}