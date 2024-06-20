package bg.sofia.uni.fmi.mjt.cocktail.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CocktailRecipesServerTest {

    CocktailRecipesServer server = CocktailRecipesServer.getNewCocktailRecipesServer();


    String createCocktail() {
        String command = "create mochito mint=3gr gin=100ml ice=30gr";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        return client.getReply();
    }

    @Test
    public void createCocktailSuccessfulTest() {
        server.start();
        String expected = "{\"status\":\"CREATED\"}";
        String actual = createCocktail();
        assertEquals(expected, actual, "Create cocktail mochito");
    }


    @Test
    public void createCocktailAlreadyExistsTest() {
        server.start();
        createCocktail();
        String expected = "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail Mochtio already exists\"}";
        String actual = createCocktail();
        assertEquals(expected, actual, "Create cocktail mochito already exists");
    }

    @Test
    public void disconnectClientTest() {
        server.start();
        String command = "disconnect";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        String expected = "Disconnected from the server";;
        String actual = client.getReply();
        assertEquals(expected, actual, "Disconnect client");
    }

    @Test
    public void unknownCommandTest() {
        server.start();
        String command = "asd";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        String expected = "Unknown command";;
        String actual = client.getReply();
        assertEquals(expected, actual, "Unknown command");
    }

    @Test
    public void getAllTest() {
        server.start();
        createCocktail();
        String command = "get all";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        String expected = "{\"status\":\"OK\",\"cocktails\":[{\"name\":\"mochito\",\"ingredients\":[{\"name\":\"mint\",\"amount\":\"3gr\"}," +
                "{\"name\":\"gin\",\"amount\":\"100ml\"},{\"name\":\"ice\",\"amount\":\"30gr\"}]}]}";
        String actual = client.getReply();
        assertEquals(expected, actual, "Get all command");
    }

    @Test
    public void getByNameTest() {
        server.start();
        createCocktail();
        String command = "get by-name mochito";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        String expected = "{\"status\":\"OK\",\"cocktails\":[{\"name\":\"mochito\",\"ingredients\":[{\"name\":\"mint\",\"amount\":\"3gr\"}," +
                "{\"name\":\"gin\",\"amount\":\"100ml\"},{\"name\":\"ice\",\"amount\":\"30gr\"}]}]}";
        String actual = client.getReply();
        assertEquals(expected, actual, "Get by-name command");
    }

    @Test
    public void getByIngridientTest() {
        server.start();
        createCocktail();
        String command = "get y-ingredient gin";
        var client = CocktailRecipesClientCreateStub.getNewCocktaiLRecipesClient(command);
        client.start();
        String expected = "{\"status\":\"OK\",\"cocktails\":[{\"name\":\"mochito\",\"ingredients\":[{\"name\":\"mint\",\"amount\":\"3gr\"}," +
                "{\"name\":\"gin\",\"amount\":\"100ml\"},{\"name\":\"ice\",\"amount\":\"30gr\"}]}]}";
        String actual = client.getReply();
        assertEquals(expected, actual, "Get by-ingredient command");
    }
}
