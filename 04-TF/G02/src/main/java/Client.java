import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import pt.haslab.ekit.Spread;
import spread.SpreadMessage;

public class Client {
    public static void main(String[] args) throws Exception {
        ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());
        Spread s = new Spread(args[0], false);

        tc.execute(() -> {
            s.open().thenRun(() -> {
                s.join("gx");

                for(int i = 1; i <= 20; i++) {
                    SpreadMessage m = new SpreadMessage();
                    m.addGroup("gx");

                    System.out.println("Sending: " + i);
                    s.multicast(m, i);
                }
            });
        }).join();
    }
}
