package ex4;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;

public class Reference<T> implements CatalystSerializable {
    private Address address;
    private int id;
    private Class<T> type;

    public Reference() {
    }

    public Reference(Address address, int id, Class<T> type) {
        this.address = address;
        this.id = id;
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(id);
        bufferOutput.writeString(address.host());
        bufferOutput.writeInt(address.port());
        bufferOutput.writeString(type.getName());
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        id = bufferInput.readInt();

        String host = bufferInput.readString();
        int port = bufferInput.readInt();
        address = new Address(host, port);

        try {
            type = (Class<T>) Class.forName(bufferInput.readString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
