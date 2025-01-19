package org.example.BufferControl.TypeOfBuffer;

public class FloatBufferType extends TypeOfBuffer {

    public FloatBufferType(int length) {
        create(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof float[] castedArr)) {
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
        if (buffer == null || !(arr instanceof float[] castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES;
    }

    @Override
    public Object getArr() {
        if (array == null) {
            array = new float[buffer.capacity() / getByteSize()];
        }
        float[] arr = (float[]) array;

        buffer.rewind();
        for (int i = 0; i < arr.length; i++){
            arr[i] = buffer.getFloat();
        }
        buffer.rewind();
        return arr;
    }

    @Override
    protected void updateArray(int length) {
        array = new float[length];
    }
}
