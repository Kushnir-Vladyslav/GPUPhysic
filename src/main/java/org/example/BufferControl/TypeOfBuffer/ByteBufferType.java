package org.example.BufferControl.TypeOfBuffer;

import java.nio.ByteBuffer;

public class ByteBufferType extends TypeOfBuffer{

    public ByteBufferType (int length) {
        super(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof byte[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }

        buffer.position(startPosition * getByteSize());
        for (float v : castedArr) {
            buffer.putFloat(v);
        }
        buffer.rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (buffer == null || !(arr instanceof byte[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Byte.SIZE;
    }

    @Override
    public Object getArr() {
        if (array == null) {
            array = new byte[buffer.capacity() / getByteSize()];
        }
        byte[] arr = (byte[]) array;

        buffer.rewind();
        for (int i = 0; i < arr.length; i++){
            arr[i] = buffer.get();
        }
        buffer.rewind();
        return arr;
    }

    @Override
    protected void updateArray(int length) {
        array = new byte[length];
    }
}
