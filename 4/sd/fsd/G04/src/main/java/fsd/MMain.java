package fsd;

public class MMain {
    public static void main(String[] args) {
        MAsyncLineBuffer lines = new MAsyncLineBuffer(sock);

        lines.read()
           .thenCompose((s)->lines.write(s))
           .thenRun(()->{System.out.println("Done!");})
            .exceptionally((t)->t.printStackTrace();).get()
    }
}