package bg.sofia.uni.fmi.mjt.cocktail.server;

public class Client {

    public static void main(String[] args) {

        CocktailRecipesClient client = CocktailRecipesClient.getNewCocktaiLRecipesClient();
        client.start();

    }
}
