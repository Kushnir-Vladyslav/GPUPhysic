package org.example.BufferControl;

import org.example.Structs.CursorPosition;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class CursorPositionBuffer extends TypeOfBuffer<ByteBuffer>{

    public CursorPositionBuffer(int length) {
        create(length);
    }

    @Override
    protected void create(int length) {
        buffer = MemoryUtil.memAlloc(getByteSize());
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!CursorPosition.class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        CursorPosition castedArr = (CursorPosition) arr;
        buffer.rewind().
                putFloat(castedArr.radius).
                putFloat(castedArr.x).
                putFloat(castedArr.y).
                rewind();
    }

    @Override
    public int getByteSize() {
        return Float.BYTES * 3;
    }

    @Override
    public long createClBuffer(long flags) {
        return 0;
    }

    @Override
    public void rewriteClBuffer(long clBuffer, long offset) {

    }

    @Override
    public void readClBuffer(long clBuffer, long offset) {

    }

    @Override
    public void getByteBuffer(ByteBuffer byteBuffer) {
        byteBuffer.rewind().put(buffer);
    }
}
