import messaging.Data.*;

public class MessageWrapper {
    private Message message;
    private int lives;

    MessageWrapper(Message message, int lives) {
        this.message = message;
        this.lives = lives;
    }

    public boolean consume() {
        lives--;
        return lives <= 0;
    }

    public Message getMessage() {
        return message;
    }
}
