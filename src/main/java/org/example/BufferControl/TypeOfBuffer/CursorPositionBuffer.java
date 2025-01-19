package org.example.BufferControl.TypeOfBuffer;

import org.example.Structs.CursorPosition;

public class CursorPositionBuffer extends TypeOfBuffer{

    public CursorPositionBuffer(int length) {
        create(length);
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (buffer == null || !(arr instanceof CursorPosition castedArr)) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }

        buffer.rewind().
                putFloat(castedArr.radius).
                putFloat(castedArr.x).
                putFloat(castedArr.y).
                rewind();
    }

    @Override
    public int getSize(Object arr) {
        return 0;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES * 3;
    }

    @Override
    public Object getArr() {
        return null;
    }

    @Override
    protected void updateArray(int length) {

    }
}
