package org.example.BufferControl.TypeOfBuffer;

public class IntBufferType extends TypeOfBuffer {

    public IntBufferType(int length) {
        super(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof int[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }

        buffer.position(startPosition * getByteSize());
        for (int j : castedArr) {
            buffer.putInt(j);
        }
        buffer.rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (buffer == null || !(arr instanceof int[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Integer.BYTES;
    }

    @Override
    public Object getArr() {
        if (array == null) {
            array = new int[buffer.capacity() / getByteSize()];
        }
        int[] arr = (int[]) array;

        buffer.rewind();
        for (int i = 0; i < arr.length; i++){
            arr[i] = buffer.getInt();
        }
        buffer.rewind();
        return arr;
    }

    @Override
    protected void updateArray(int length) {
        array = new int[length];
    }
}
