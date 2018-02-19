import ifaces.IBank;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Client extends Thread {
    IBank bank;
    AtomicInteger balance;

    public Client(IBank bank, AtomicInteger balance) {
        this.bank = bank;
        this.balance = balance;
    }

    public void run() {
        int tBalance = 0;

        int min = -100, max = 100;
        Random r = new Random();

        for(int i = 0; i < 5000; i++) {
            int amount = r.nextInt(max - min + 1) + min;

            if (bank.move(amount))
                tBalance += amount;
        }

        balance.addAndGet(tBalance);
    }
}
