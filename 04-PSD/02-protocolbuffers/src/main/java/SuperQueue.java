import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import messaging.Data.*;

/**
 * When a message is sent, append it to the end of the list and notify all users that a 
 * new message is available.
 *
 * The queue has a limited size and should never block. If a message is received and the
 * queue is full, it should drop the oldest message, even if there are users that have not
 * read it yet.
 *
 * When a user connects, he should have no access to the previous messages. Therefore, the 
 * first message he will receive is a message that is not yet in the queue.
 *
 * The system does not save a state of each user. Each user should know which message he 
 * wants to read next.
 * 
 * When a message is saved, it should also save a counter of how many users are connected.
 * Once all the users have read that message, the message shall be deleted.
 *
 * The message requested by an user is known by calculating the difference of the last
 * message received with the index of the requested message.
 *
 * When a user requests a message and that message was already deleted (queue have dropped a
 * message due to lack of capacity), the first message available shall be read instead.
 *
 * The system should always know how many users are connected so that it can correctly
 * know the lives of a message.
 *
 * The system should be able to handle user's disconnections, freeing the resources of that
 * user and maybe consuming the messages that it was supposed to receive.
 */

public class SuperQueue {
    private List<MessageWrapper> messages;
    private int capacity;
    private int clients = 0;
    private int counter = 0;

    private Lock lock = new ReentrantLock();
    private Condition noMessages = lock.newCondition();

    public SuperQueue(int capacity) {
        this.messages = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public int connect() {
        lock.lock(); 
        try {
            clients += 1; 
            return counter;
        } finally {
            lock.unlock();
        }
    }

    public void disconnect(int nextMessage) {
        lock.lock();
        try {
            int from = messages.size() - (counter - nextMessage);
            int to = messages.size();

            for(MessageWrapper m: messages.subList(from, to)) {
                m.consume();
            }

            clients -= 1;
        } finally {
            lock.unlock();
        }
    }

    public int reconnect() {
        lock.lock();
        try {
            return counter - messages.size();
        } finally {
            lock.unlock();
        }
    }

    public void write(Message message) {
        lock.lock();
        try {
            if (messages.size() == capacity) {
                messages.remove(0);
            }

            messages.add(new MessageWrapper(message, clients));
            counter++;

            noMessages.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Message read(int messageId) throws InterruptedException, MessageDeletedException {
        lock.lock();
        try {
            while(messageId == counter) {
                noMessages.await();
            }

            int index = messages.size() - (counter - messageId);
            if (index < 0) {
                throw new MessageDeletedException("Message delete due to lack of capacity");
            }

            MessageWrapper msg = messages.get(index);
            if (msg.consume()) {
                messages.remove(msg);
            }

            return msg.getMessage();
        } finally {
            lock.unlock();
        }
    }
}
