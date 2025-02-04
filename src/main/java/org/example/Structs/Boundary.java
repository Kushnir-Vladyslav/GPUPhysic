package org.example.Structs;

import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.BoundaryBuffer;
import org.example.GLOBAL_STATE;

import static org.example.GLOBAL_STATE.bufferManager;

public class Boundary {
    public float width;
    public float height;
    public float borderThickness;
    public float sphereX;
    public float sphereY;
    public float sphereRadius;

    SingleValueBuffer<BoundaryBuffer> boundaryBuffer;

    public Boundary () {
        width = GLOBAL_STATE.WorkZoneWidth;
        height = GLOBAL_STATE.WorkZoneHeight;

        borderThickness = 2;

        sphereX = width * 0.5f;
        sphereY = height * 0.3f;

        sphereRadius = height * 0.71f;

        update();
    }

    public void update() {
        if (boundaryBuffer == null) {
            if (bufferManager.isExist("BoundaryBuffer")) {
                boundaryBuffer = bufferManager.getBuffer("BoundaryBuffer", SingleValueBuffer.class, BoundaryBuffer.class);
            } else {
                boundaryBuffer = bufferManager.createBuffer("BoundaryBuffer", SingleValueBuffer.class, BoundaryBuffer.class);
                boundaryBuffer.init();
            }
        }

        boundaryBuffer.setData(this);
    }
}
