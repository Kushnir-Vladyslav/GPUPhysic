package org.example.BufferControl.TypeOfBuffer;

import org.example.Structs.Boundary;

public class BoundaryBuffer extends TypeOfBuffer{

    public BoundaryBuffer(int length) {
        create(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof Boundary castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }

        buffer.rewind().
                putFloat(castedArr.width).
                putFloat(castedArr.height).
                putFloat(castedArr.borderThickness).
                putFloat(castedArr.sphereX).
                putFloat(castedArr.sphereY).
                putFloat(castedArr.sphereRadius).
                rewind();
    }

    @Override
    public int getSize(Object arr) {
        return 0;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES * 6;
    }

    @Override
    public Object getArr() {
        return null;
    }

    @Override
    protected void updateArray(int length) {

    }
}
