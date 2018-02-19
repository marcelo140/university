import ifaces.IBank;
import stubs.BankStub;

import java.util.concurrent.atomic.AtomicInteger;

public class Tester {
    private static final int nClients = 100;

    public static void main(String[] args) throws Exception {
        AtomicInteger balance = new AtomicInteger(0);
        IBank bank = new BankStub();

        Thread[] threads = new Thread[nClients];

        for(int i = 0; i < nClients; i++) {
            threads[i]  = new Client(bank, balance);
            threads[i].start();
        }

        for(int i = 0; i < nClients; i++)
            threads[i].join();

        System.out.println("Bank balance: " + bank.balance());
        System.out.println("Theoretical balance: " + balance.get());
    }
}
