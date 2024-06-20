package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;

public class CommandFactory {
    public static final String CREATE_COMMAND = "create";
    public static final String GET_COMMAND = "get";
    public static final String DISCONNECT_COMMAND = "disconnect";
    private static final String COMMAND_SEPARATOR = " ";


    public static Command of(String query) {
        String[] splitCommand = query.split(COMMAND_SEPARATOR);
        return switch(splitCommand[0]) {
            case CREATE_COMMAND -> new CreateCommand(splitCommand);
            case GET_COMMAND -> new GetCommand(splitCommand);
            case DISCONNECT_COMMAND -> new DisconnectCommand();
            default -> new InvalidCommand();
        };
    }
}
