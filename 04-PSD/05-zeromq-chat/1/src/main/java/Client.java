import org.zeromq.ZMQ;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No port provided");
            return;
        }

        ZMQ.Context context = ZMQ.context(1);
        Publisher pub = new Publisher(context, args[0]);
        Subscriber sub = new Subscriber(context);

        pub.run();
        sub.run();

        pub.join();
        sub.join();
        context.term();
    }
}
