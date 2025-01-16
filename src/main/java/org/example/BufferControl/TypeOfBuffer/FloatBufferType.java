package org.example.BufferControl.TypeOfBuffer;

public class FloatBufferType extends TypeOfBuffer {

    public FloatBufferType(int length) {
        create(length);
        array = new float[length];
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!float[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        float[] castedArr = (float[]) arr;

        buffer.position(startPosition * getByteSize());
        for (float v : castedArr) {
            buffer.putFloat(v);
        }
        buffer.rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (!float[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        float[] castedArr = (float[]) arr;
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
}
