package ex5;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Authenticator {
    private final AsyncLineBuffer buf;
    private final Chat chat;
    private boolean isAuthenticated = false;

    Authenticator(AsynchronousSocketChannel ch, Chat chat) {
        this.buf = new AsyncLineBuffer(ch);
        this.chat = chat;
    }

    public CompletableFuture<Void> authenticate() {
        return recursiveAuthenticate(new CompletableFuture<>());
    }

    private CompletableFuture<Void> recursiveAuthenticate(CompletableFuture<Void> r) {
        buf.read()
           .thenCompose(this::handle)
           .thenAccept((success) ->
           {
                if (!success)
                    recursiveAuthenticate(r);
                else
                    r.complete(null);
           });

        return r;
    }

    private CompletableFuture<Boolean> handleLogin() {
        CompletableFuture<Boolean> r = new CompletableFuture<>();

        CompletableFuture<String> username = buf.read();
        CompletableFuture<String> password = username
                .thenCompose(t -> buf.read());

        username.thenAcceptBoth(password, (usr, psw) -> {
            if (chat.login(usr,psw)) {
                isAuthenticated = true;
                buf.write("Login successful");

                r.complete(true);
            }
            else
                r.complete(false);
        });

        return r;
    }

    private CompletableFuture<Boolean> handleRegistration() {
        CompletableFuture<Boolean> r = new CompletableFuture<>();

        CompletableFuture<String> username = buf.read();
        CompletableFuture<String> password = username
                .thenCompose(t -> buf.read());

        username.thenAcceptBoth(password, (usr, psw) -> {
            if (chat.register(usr, psw)) {
                isAuthenticated = true;
                buf.write("Registration successful");

                r.complete(true);
            } else {
                r.complete(false);
            }
        });

        return r;
    }

    public CompletableFuture<String> read() {
        if (!isAuthenticated)
            return null;

        return buf.read();
    }

    public CompletableFuture<Void> write(String str) {
        if (!isAuthenticated)
            return null;

        return buf.write(str);
    }

    private CompletionStage<Boolean> handle(String req) {
        if (req.equals("register"))
            return handleRegistration();
        else if (req.equals("login"))
            return handleLogin();
        else {
            CompletableFuture<Boolean> tmp = new CompletableFuture();
            tmp.complete(false);
            return tmp;
        }
    }
}