package org.example.BufferControl.TypeOfBuffer;

public class FloatBufferType extends TypeOfBuffer {

    public FloatBufferType(int length) {
        create(length);
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
    public Object getArr(int length) {
        if (length > buffer.capacity() / Integer.BYTES) {
            throw new IllegalArgumentException("The buffer size is smaller than requested.");
        }
        float[] arr = new float[length];
        buffer.rewind();
        for (int i = 0; i < length; i++){
            arr[i] = buffer.getFloat();
        }
        buffer.rewind();
        return arr;
    }
}
