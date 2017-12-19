package ex4;

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
        auth.authenticate(null, ach);
    }

    private CompletionHandler<Void, Void> ach = new CompletionHandler<>() {
        @Override
        public void completed(Void result, Void attachment) {
            auth.read(null, rch);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {

        }
    };

    private CompletionHandler<Void, Void> wch = new CompletionHandler<>() {
        @Override
        public void completed(Void v1, Void v2) {
            synchronized (out) {
                out.remove();
                String msg = out.peek();
                if (msg != null) {
                    auth.write(msg, null, wch);
                }
            }
        }

        @Override
        public void failed(Throwable e, Void v) {
        }
    };

    public void send(String s) {
        synchronized (out) {
            boolean wasEmpty = out.isEmpty();

            out.add(s);
            if (wasEmpty) {
                auth.write(s, null, wch);
            }
        }
    }

    private CompletionHandler<String, Void> rch = new CompletionHandler<>() {
        @Override
        public void completed(String str, Void v) {
            chat.send(str);

            auth.read(null, this);
        }

        @Override
        public void failed(Throwable t, Void v) {
        }
    };
}
