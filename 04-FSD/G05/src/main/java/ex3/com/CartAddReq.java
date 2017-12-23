package ex3.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class CartAddReq implements CatalystSerializable {
    private int cartId;
    private int isbn;

    public CartAddReq() {
    }

    public CartAddReq(int cartId, int isbn) {
        this.cartId = cartId;
        this.isbn = isbn;
    }

    public int getCartId() {
        return cartId;
    }

    public int getIsbn() {
        return isbn;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(cartId);
        bufferOutput.writeInt(isbn);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        cartId = bufferInput.readInt();
        isbn = bufferInput.readInt();
    }
}
