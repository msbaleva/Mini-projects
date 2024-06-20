package bg.sofia.uni.fmi.mjt.cocktail.server;

public class Server {

    public static void main(String[] args) {

        CocktailRecipesServer server = CocktailRecipesServer.getNewCocktailRecipesServer();
        server.start();

    }
}
