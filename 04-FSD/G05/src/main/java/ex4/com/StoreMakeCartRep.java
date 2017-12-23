package ex4.com;

import ex4.Reference;
import ex4.stubs.RemoteCart;
import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreMakeCartRep implements CatalystSerializable {
    private Reference<RemoteCart> cartRef;

    public StoreMakeCartRep() {

    }

    public StoreMakeCartRep(Reference<RemoteCart> cartRef) {
        this.cartRef = cartRef;
    }

    public Reference<RemoteCart> getCartRef() {
        return cartRef;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        serializer.writeObject(cartRef, bufferOutput);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        cartRef = serializer.readObject(bufferInput);
    }
}
