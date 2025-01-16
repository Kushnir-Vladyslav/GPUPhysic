package org.example.BufferControl.TypeOfBuffer;

public class IntBufferType extends TypeOfBuffer {

    public IntBufferType(int length) {
        create(length);
        array = new int[length];
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = int[].class.cast(arr);

        buffer.position(startPosition * getByteSize());
        for (int j : castedArr) {
            buffer.putInt(j);
        }
        buffer.rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = (int[]) arr;
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
}
