package ex3;

import java.util.PriorityQueue;

public class PackageQueue {
    PriorityQueue<Package>[] queue;

    PackageQueue(int size) {
        queue = new PriorityQueue[size];

        for(int i = 0; i < size; i++)
            queue[i] = new PriorityQueue<>();
    }

    public void add(int i, Package pkg) {
        queue[i].add(pkg);
    }

    public boolean hasReady(Clock c) {
        for(int i = 0; i < queue.length; i++) {
            Package pkg = queue[i].peek();

            if (pkg != null && pkg.getClock().compareTo(c) <= 0)
                return true;
        }

        return false;
    }

    public Package getReady(Clock c) {
        for(int i = 0; i < queue.length; i++) {
            Package pkg = queue[i].peek();

            if (pkg != null && pkg.getClock().compareTo(c) <= 0)
                return queue[i].poll();
        }

        return null;
    }
}
