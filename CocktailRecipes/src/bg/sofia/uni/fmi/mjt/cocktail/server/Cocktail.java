package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record Cocktail(String name, Set<Ingredient> ingredients) {

    public static Cocktail of(String[] recipe) {
        String name = recipe[1];
        Set<Ingredient> ingredients = Arrays.stream(recipe)
                .skip(2)
                .map(Ingredient::of)
                .collect(Collectors.toSet());
        return new Cocktail(name, ingredients);
    }

    @Override
    public String toString() {
        return String.format("{\"name\":\"%s\",\"ingredients\":%s}", name, ingredients);
    }
}