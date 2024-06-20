package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class CocktailRecipesClientCreateStub extends CocktailRecipesClient {

    private String command;
    private String reply;

    private CocktailRecipesClientCreateStub(String command) {
        this.command = command;
    }

    public static CocktailRecipesClientCreateStub getNewCocktaiLRecipesClient(String command) {
        return new CocktailRecipesClientCreateStub(command);
    }

    public String getReply() {
        return reply;
    }

    @Override
    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open()) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            buffer.clear();
            buffer.put(command.getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();
            this.reply = new String(buffer.array(), 0, buffer.limit());
        } catch (IOException e) {
            throw new RuntimeException(IOEXCEPTION_MESSAGE, e);
        }

    }
}
