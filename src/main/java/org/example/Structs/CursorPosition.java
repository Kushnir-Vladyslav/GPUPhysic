package org.example.Structs;

import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;

import static org.example.GLOBAL_STATE.bufferManager;

public class CursorPosition {
    public float x;
    public float y;
    public float radius;

    private final float sizeOfCursor = 10;

    SingleValueBuffer<CursorPositionBuffer> cursorBuffer;

    public CursorPosition () {
        x = 0;
        y = 0;
        radius = 0;
    }

    public void setCursorPosition (float x, float y) {
        this.x = x;
        this.y = y;
        radius = sizeOfCursor;

        update();
    }

    public void inactivateCursor () {
        radius = 0;

        update();
    }

    public void update () {
        if (cursorBuffer == null) {
            if (bufferManager.isExist("CursorBuffer")) {
                cursorBuffer = bufferManager.getBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);
            } else {
                cursorBuffer = bufferManager.createBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);
                cursorBuffer.init();
            }
        }

        cursorBuffer.setData(this);
    }
}
