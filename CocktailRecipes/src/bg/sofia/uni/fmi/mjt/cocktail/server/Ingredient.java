package bg.sofia.uni.fmi.mjt.cocktail.server;

public record Ingredient(String name, String amount) {

    private static final String RECIPE_INFO_SEPARATOR = "=";

    public static Ingredient of(String recipe) {
        String[] recipeInfo = recipe.split(RECIPE_INFO_SEPARATOR);
        return new Ingredient(recipeInfo[0], recipeInfo[1]);
    }

    @Override
    public String toString() {
        return String.format("{\"name\":\"%s\",\"amount\":\"%s\"}", name, amount);
    }
}
