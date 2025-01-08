package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public abstract class TypeOfBuffer <BU extends Buffer> {
    protected BU buffer;

    protected abstract void create (int length);

    public abstract void set (Object arr);

    public abstract void set (Object arr, int startPosition);

    public abstract int getSize(Object arr);

    public abstract int getByteSize();

    public abstract Object getArr(int length);

    public abstract long createClBuffer(long flags);

    public abstract void rewriteClBuffer(long clBuffer, long offset);

    public abstract void readClBuffer(long clBuffer, long offset);

    public void reSize (int newLength) {
        destroy();
        create(newLength);
    }

    public void destroy (){
        if (buffer != null) {
            MemoryUtil.memFree(buffer);
            buffer = null;
        }
    }

    public void getByteBuffer(ByteBuffer byteBuffer) {
        throw new IllegalStateException("The getByteBuffer method is not implemented.");
    }
}
