package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;


public class CocktailRecipesServer {

    public static final int SERVER_PORT = 2020;
    public static final int BUFFER_SIZE = 1024;
    public static final int SLEEP_MILLIS = 500;
    public static final String SERVER_HOST = "localhost";
    public static final String BUFFER_ERROR_MESSAGE = "Problem reading from buffer";
    public static final String IOEXCEPTION_MESSAGE = "A problem occurred with the chat server.";
    public static final String WAITING_USERS_MESSAGE = "Waiting for users to log.";
    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private Set<SocketChannel> activeClients = new HashSet<>();

    public static CocktailRecipesServer getNewCocktailRecipesServer() {
        return new CocktailRecipesServer();
    }

    private void sendToChannel(String message, SocketChannel userChannelRecipient) {
        buffer.clear();
        buffer.put(message.getBytes());
        System.out.println(message);
        buffer.flip();
        try {
            userChannelRecipient.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(BUFFER_ERROR_MESSAGE, e);
        }
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            boolean running = true;
            while (running) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    System.out.println(WAITING_USERS_MESSAGE);
                    try {
                        Thread.sleep(SLEEP_MILLIS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        buffer.clear();
                        int read = socketChannel.read(buffer);
                        if (read <= 0) {
                            //System.out.println("nothing to read, will close channel");
                            socketChannel.close();
                            break;
                        }

                        buffer.flip();
                        String readMessage = new String(buffer.array(), 0, buffer.limit());
                        sendToChannel(CommandFactory.of(readMessage).execute(), socketChannel);
                        if (readMessage.startsWith(CommandFactory.DISCONNECT_COMMAND)) {
                            activeClients.remove(socketChannel);
                        }


                    } else if (key.isAcceptable()) {
                        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = socketChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(IOEXCEPTION_MESSAGE, e);
        }
    }
}
