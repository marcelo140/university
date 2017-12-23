package ex4.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class CartBuyRep implements CatalystSerializable {
    private boolean success;

    public CartBuyRep() {
    }

    public CartBuyRep(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeBoolean(success);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        success = bufferInput.readBoolean();
    }
}
