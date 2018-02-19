package com;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class BankMoveRep implements CatalystSerializable {
    private boolean res;

    private BankMoveRep() {
    }

    public BankMoveRep(boolean res) {
        this.res = res;
    }

    public boolean isRes() {
        return res;
    }

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeBoolean(res);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        res = bufferInput.readBoolean();
    }
}
