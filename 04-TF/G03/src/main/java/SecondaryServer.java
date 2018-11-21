import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import pt.haslab.ekit.Spread;
import spread.SpreadMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class SecondaryServer {
    public static void main(String[] args) throws Exception {
        AtomicInteger balance = new AtomicInteger(0);

        Spread s = new Spread("scnd", false);
        ThreadContext tc = new SingleThreadContext("spr-%d", new Serializer());

        tc.execute(() -> {
            s.open();
            s.join("primary");

            s.handler(Integer.class, (msg, data) -> {
                balance.addAndGet(data);

                SpreadMessage reply = new SpreadMessage();
                reply.addGroup("primary");

                s.multicast(reply, "ACK");
            });
        });
    }
}
