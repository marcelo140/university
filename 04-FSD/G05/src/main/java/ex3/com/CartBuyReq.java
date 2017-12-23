package ex3.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class CartBuyReq implements CatalystSerializable {
    private int cartId;

    public CartBuyReq() {
    }

    public CartBuyReq(int cartId) {
        this.cartId = cartId;
    }

    public int getCartId() {
        return cartId;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(cartId);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        cartId = bufferInput.readInt();
    }
}
