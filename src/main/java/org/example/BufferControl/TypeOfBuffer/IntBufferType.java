package org.example.BufferControl.TypeOfBuffer;

public class IntBufferType extends TypeOfBuffer {

    public IntBufferType(int length) {
        create(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = int[].class.cast(arr);
        for (int i = 0; i < castedArr.length; i++){
            buffer.putInt(i + startPosition, castedArr[i]);
        }
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
    public Object getArr(int length) {
        if (length > buffer.capacity() / Integer.BYTES) {
            throw new IllegalArgumentException("The buffer size is smaller than requested.");
        }
        int[] arr = new int[length];
        buffer.rewind();
        for (int i = 0; i < length; i++){
            arr[i] = buffer.getInt();
        }
        buffer.rewind();
        return arr;
    }
}
