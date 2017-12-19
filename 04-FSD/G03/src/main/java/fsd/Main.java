package fsd;

import java.nio.channels.CompletionHandler;

public class Main {
    public static void main(String[] args) {
        AsyncLogin l = new AsyncLogin(new AsyncLineBuffer(sock));
        
        l.read(null, new CompletionHandler<String, Object>() {
            @Override
            public void completed(String s, Object o) {
                System.out.println("linha: " + l);
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        });
    }
}
