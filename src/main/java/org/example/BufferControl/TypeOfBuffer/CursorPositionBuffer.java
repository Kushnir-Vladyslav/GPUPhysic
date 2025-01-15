package org.example.BufferControl.TypeOfBuffer;

import org.example.Structs.CursorPosition;

public class CursorPositionBuffer extends TypeOfBuffer{

    public CursorPositionBuffer(int length) {
        create(length);
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
    public int getSize(Object arr) {
        return 0;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES * 3;
    }

    @Override
    public Object getArr(int length) {
        return null;
    }
}
