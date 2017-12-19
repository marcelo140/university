package push_pull;
import org.zeromq.ZMQ;

public class Source {
  public static void main(String[] args) throws Exception {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket source = context.socket(ZMQ.PUSH);
    source.bind("tcp://*:" + args[0]);
    //Thread.sleep(1000);  // wait for workers to connect
    ZMQ.Socket cli = context.socket(ZMQ.REQ);
    cli.connect("tcp://localhost:" + args[1]);
    int nMsgs = new Integer(args[2]);
    cli.send(args[2]);
    cli.recv(); // receive reply
    cli.close();
    for (int i = 0; i < nMsgs; i++) {
      String str = String.valueOf(i);
      source.send(str);
      System.out.println("Sent " + str);
    }
    source.close();
    context.term();
  }
}

