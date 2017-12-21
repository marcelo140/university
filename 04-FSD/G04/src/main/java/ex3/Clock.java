package ex3;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

import java.util.Arrays;

public class Clock implements Comparable<Clock>, CatalystSerializable {
    private int me, peers;
    private Integer[] clocks;

    Clock() {

    }

    Clock(int me, int peers) {
        this.me = me;
        this.peers = peers;

        clocks = new Integer[peers];
        for(int i = 0; i < peers; i++)
            clocks[i] = 0;
    }

    public void increment() {
        clocks[me]++;
    }

    public void update(Clock others) {
        for(int i = 0; i < peers; i++) {
            if (others.clocks[i] > clocks[i])
                clocks[i] = others.clocks[i];
        }
    }

    public int compareTo(Clock other) {
        for(int i = 0; i < peers; i++) {
            if (i == me || other.clocks[i] == clocks[i])
                continue;

            return clocks[i] - other.clocks[i];
        }

        return 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(clocks);
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(me);
        bufferOutput.writeInt(peers);

        for(int i = 0; i < peers; i++)
            bufferOutput.writeInt(clocks[i]);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        me = bufferInput.readInt();
        peers = bufferInput.readInt();

        clocks = new Integer[peers];
        for(int i = 0; i < peers; i++)
            clocks[i] = bufferInput.readInt();
    }
}
