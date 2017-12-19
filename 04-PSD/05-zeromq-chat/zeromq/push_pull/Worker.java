package push_pull;
import org.zeromq.ZMQ;

public class Worker {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket source = context.socket(ZMQ.PULL);
    ZMQ.Socket sink = context.socket(ZMQ.PUSH);
    source.connect("tcp://localhost:" + args[0]);
    sink.connect("tcp://localhost:" + args[1]);
    while (true) {
      byte[] b = source.recv();
      int n = new Integer(new String(b));
      System.out.println("Received " + n);
      String res = String.valueOf(2 * n);
      sink.send(res);
    }
    //source.close();
    //sink.close();
    //context.term();
  }
}

