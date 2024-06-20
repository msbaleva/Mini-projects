package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class CocktailRecipesClient {

    public static final int SERVER_PORT = 2020;
    public static final String SERVER_HOST = "localhost";
    public static final int BUFFER_SIZE = 1024;
    public static final String IOEXCEPTION_MESSAGE = "A problem occurred with the client.";
    public static final String PROMPT = "=> ";
    public static final String WELCOME_MESSAGE = "< Welcome to Cocktail Recipes >" ;
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static CocktailRecipesClient getNewCocktaiLRecipesClient() {
        return new CocktailRecipesClient();
    }

    public void start(InputStream is, PrintStream ps) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(is)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            ps.println(WELCOME_MESSAGE);

            boolean running = true;
            while (running) {
                ps.print(PROMPT);
                String message = scanner.nextLine();

                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                socketChannel.read(buffer);
                buffer.flip();
                String reply = new String(buffer.array(), 0, buffer.limit());
                ps.println(PROMPT + reply);
                if (message.equals(CommandFactory.DISCONNECT_COMMAND)) {
                    running = false;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(IOEXCEPTION_MESSAGE, e);
        }
    }

    public void start() {
        start(System.in, System.out);
    }

}
