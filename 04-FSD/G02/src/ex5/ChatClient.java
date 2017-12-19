package ex5;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;

public class ChatClient {
    private Authenticator auth;
    private Chat chat;
    private Queue<String> out = new LinkedList<>();

    ChatClient(AsynchronousSocketChannel ch, Chat chat) {
        this.auth = new Authenticator(ch, chat);
        this.chat = chat;

        // When a client connects, start reading from the socket immediately
        auth.authenticate()
            .thenCompose(t -> auth.read())
            .thenAccept(this::readHandler);
    }

    public void writeHandler(Void trash) {
        synchronized (out) {
            out.remove();
            String msg = out.peek();
            if (msg != null) {
                auth.write(msg)
                    .thenAccept(this::writeHandler);
            }
        }
    }

    public void send(String s) {
        synchronized (out) {
            boolean wasEmpty = out.isEmpty();

            out.add(s);
            if (wasEmpty) {
                auth.write(s)
                    .thenAccept(this::writeHandler);
            }
        }
    }

    private void readHandler(String str) {
        chat.send(str);
        auth.read()
            .thenAccept(this::readHandler);
    }
}
