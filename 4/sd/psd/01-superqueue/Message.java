public class Message {
    private String content;
    private int lives;

    Message(String content, int lives) {
        this.content = content;
        this.lives = lives;
    }

    public boolean consume() {
        lives--;
        return lives <= 0;
    }

    public String getContent() {
        return content;
    }
}
