package fsd;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class AsyncLogin {
    private AsyncLineBuffer lines;
    private boolean login = false;

    public CompletableFuture<Void> read(final A value, CompletionHandler<String, A> handler) {
        CompletableFuture future = new CompletableFuture();

        if (login)
            lines.read(value, handler);
        else {
            lines.write("login: ", new CompletionHandler<Void, Object>() {
                @Override
                public void completed(Void aVoid, Object o) {
                    lines.read(null, new CompletionHandler<>() {
                        @Override
                        public void completed(Object o, Object o2) {
                            lines.write("password: ", new CompletionHandler<Void, Object>() {
                            })
                        }

                        @Override
                        public void failed(Throwable throwable, Object o) {

                        }
                    })
                }

                @Override
                public void failed(Throwable throwable, Object o) {

                }
            })
        }
    }

    private CompletionHandler<String, Object> wHandler;
    private Object wValue;

    public <A> void write(String line, CompletionHandler<Void, A> handler) {
		...
    }
}
