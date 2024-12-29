package org.example.Structs;

public class CursorPosition {
    public float x;
    public float y;
    public float radius;

    private float sizeOfCursor;

    public CursorPosition () {
        x = 0;
        y = 0;
        radius = 0;
    }

    public void setCursorPosition (float x, float y) {
        this.x = x;
        this.y = y;
        radius = sizeOfCursor;
    }

    public void inactivateCursor () {
        radius = 0;
    }
}
