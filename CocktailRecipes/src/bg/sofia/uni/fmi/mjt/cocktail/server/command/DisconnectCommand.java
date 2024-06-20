package bg.sofia.uni.fmi.mjt.cocktail.server.command;

public class DisconnectCommand implements Command {


    private static final String DISCONNECTED_MESSAGE = "Disconnected from the server";
    @Override
    public String execute() {
        return DISCONNECTED_MESSAGE;
    }
}
