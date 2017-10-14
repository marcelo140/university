public class ActiveToken {
    private boolean active = true;

    synchronized public boolean isActive() {
        return active;
    }

    synchronized public void deactivate() {
        active = false;
    }
}

