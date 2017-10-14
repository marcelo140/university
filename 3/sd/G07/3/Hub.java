import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class Hub {
	Lock lock;
	Condition noMessages;
	List<Message> messages;

	Hub() {
		messages = new ArrayList<>();

		lock = new ReentrantLock();
		noMessages = lock.newCondition();
	}

	public void send(Socket sock, String message) {
		lock.lock();
		messages.add(new Message(sock, message));
		noMessages.signalAll();
		lock.unlock();
	}

	public Message receive(int messageID) throws InterruptedException {
		lock.lock();
		while (messageID == messages.size())
			noMessages.await();
		lock.unlock();

		return messages.get(messageID);
	}

	public int nextMessage() {
		int nextMessageID;

		lock.lock();
		nextMessageID = messages.size();
		lock.unlock();

		return nextMessageID;
	}
}
