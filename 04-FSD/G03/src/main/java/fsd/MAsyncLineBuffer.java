package fsd;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class MAsyncLineBuffer {
    private ByteBuffer buf, line;

    private AsynchronousSocketChannel sock;

    public CompletableFuture<String> read() {
        return readRecursive(new CompletableFuture<>());
    }

    private CompletableFuture<String> readRecursive(CompletableFuture<String> r) {
        while(buf.hasRemaining()) {
            byte c = buf.get();
            if (c == '\n') {
                line.flip();

                byte b[] = new byte[line.remaining()];
                buf.get(b);
                r.complete(new String(b));

                return r;
            } else {
                line.put(c);
            }
        }

        buf.clear();

        sock.read(buf, new CompletionHandler<>() {
            @Override
            public void completed(Object o, Object o2) {
                readRecursive(r);
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        });

        return r;
    }

    private CompletableFuture<Void> write(String line) {
        sock.write(buf, new CompletionHandler<>() {
            @Override
            public void completed(Object o, Object o2) {
                if (buf.hasRemaining()) {
                    
                } else {
                }
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        });
    }

    public CompletableFuture<Void> write(String line) {
        ByteBuffer buf = ByteBuffer.wrap(line.getBytes());
        writeRecursive(buf);
    }
}
