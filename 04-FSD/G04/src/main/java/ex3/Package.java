package ex3;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class Package implements Comparable<Package>, CatalystSerializable {
    private String message;
    private Clock clock;

    Package() {

    }

    Package(String message, Clock clock) {
        this.message = message;
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public String getMessage() {
        return message;
    }

    public int compareTo(Package other) {
        return clock.compareTo(other.clock);
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeString(message);
        serializer.writeObject(clock, bufferOutput);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        message = bufferInput.readString();
        clock = serializer.readObject(bufferInput);
    }
}
