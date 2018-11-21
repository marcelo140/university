import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import pt.haslab.ekit.Spread;
import spread.SpreadMessage;

public class Server {
    int counter = 0, buffer = 0;
    boolean active = false, buffering = false;
    String name;

    Server(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        Server srv = new Server(args[0]);
        ThreadContext tc = new SingleThreadContext("tc-%d", new Serializer());
        Spread s = new Spread(srv.name, false);

        if (args.length > 1)
            srv.active = true;

        tc.execute(() -> {
            s.handler(Integer.class, (m, v) -> {
                if (srv.active) {
                    srv.counter += v;
                    System.out.println("Current counter value: " + srv.counter);
                } else if (srv.buffering){
                    srv.buffer += v;
                    System.out.println("Buffer value:" + srv.buffer);
                }
            });
            s.handler(State.class, (m, v) -> {
                if (!srv.active) {
                    srv.active = true;
                    srv.buffering = false;
                    srv.counter = v.state + srv.buffer;
                    System.out.println("Recovered state: " + srv.counter);
                }
            });
            s.handler(String.class, (m, v) -> {
                if (srv.active) {
                    SpreadMessage msg = new SpreadMessage();
                    msg.addGroup(v);

                    s.multicast(m, new State(srv.counter));
                }

                if (v.equals(srv.name)) {
                    srv.buffering = true;
                }
            });

            s.open().thenRun(() -> {
                s.join("gx");
                if (!srv.active)
                    srv.reqState(s);
            });
        }).join();
    }

    public void reqState(Spread s) {
        SpreadMessage m = new SpreadMessage();
        m.addGroup("gx");
        m.setAgreed();

        s.multicast(m, name);
    }
}
