package bg.sofia.uni.fmi.mjt.cocktail.server.command;

public class InvalidCommand implements Command {

    private static final String INVALID_COMMAND_MESSAGE = "Unknown command";
    @Override
    public String execute() {
        return INVALID_COMMAND_MESSAGE;
    }
}
