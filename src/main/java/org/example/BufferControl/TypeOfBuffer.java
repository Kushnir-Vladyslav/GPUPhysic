package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public abstract class TypeOfBuffer <BU extends Buffer> {
    protected BU buffer;

    protected abstract void create (int length);

    public void set (Object arr) {
        set(arr, 0);
    }

    public abstract void set (Object arr, int startPosition);

    public int getSize(Object arr) {
        throw new IllegalStateException("The getSize method is not implemented.");
    }

    public abstract int getByteSize();

    public Object getArr(int length) {
        throw new IllegalStateException("The getArr method is not implemented.");
    }

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
