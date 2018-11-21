import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class State implements CatalystSerializable {
    int state;

    public State() {

    }

    public State(int state) {
        this.state = state;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(state);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        state = bufferInput.readInt();
    }
}
