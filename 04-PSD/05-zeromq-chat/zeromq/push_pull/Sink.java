package push_pull;
import org.zeromq.ZMQ;

public class Sink {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket sink = context.socket(ZMQ.PULL);
    ZMQ.Socket srv = context.socket(ZMQ.REP);
    sink.bind ("tcp://*:" + args[0]);
    srv.bind("tcp://*:" + args[1]);
    byte[] b = srv.recv();
    int nMsgs = new Integer(new String(b));
    System.out.println("Going to receive " + nMsgs + " messages");
    srv.send(""); // reply as ack
    srv.close();
    int sum = 0;
    for (int i = 0; i < nMsgs; i++) {
      b = sink.recv();
      String str = new String(b);
      System.out.println("Received " + str);
      sum += Integer.parseInt(str);
    }
    sink.close();
    context.term();
    System.out.println(sum);
  }
}

