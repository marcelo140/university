package ex4.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreMakeCartReq implements CatalystSerializable {
    public StoreMakeCartReq() {
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {

    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {

    }
}
