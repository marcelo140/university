package ex4.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreSearchReq implements CatalystSerializable {
    private String title;

    public StoreSearchReq() {
    }

    public StoreSearchReq(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeString(title);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        title = bufferInput.readString();
    }
}
