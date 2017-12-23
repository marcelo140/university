package ex4.com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreSearchRep implements CatalystSerializable {
    private int isbn;

    public StoreSearchRep() {
    }

    public StoreSearchRep(int isbn) {
        this.isbn = isbn;
    }

    public int getIsbn() {
        return isbn;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(isbn);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        isbn = bufferInput.readInt();
    }
}
