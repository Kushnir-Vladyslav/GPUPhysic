package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.BoundaryBuffer;
import org.example.BufferControl.TypeOfBuffer.DataExchangeStruct.Boundary;

public class BoundaryController {
    private static BoundaryController boundaryController;

    BufferManager bufferManager;

    SingleValueBuffer<BoundaryBuffer> boundaryBuffer;

    private Boundary boundary;

    public static BoundaryController getInstance() {
        if (boundaryController == null) {
            boundaryController = new BoundaryController();
        }

        return boundaryController;
    }

    private BoundaryController () {
        bufferManager = BufferManager.getInstance();

        boundary = new Boundary();

        boundary.width = Canvas.getCanvasWidth();
        boundary.height = Canvas.getCanvasHeight();

        boundary.borderThickness = 2;

        boundary.sphereX = boundary.width * 0.5f;
        boundary.sphereY = boundary.height * 0.3f;

        boundary.sphereRadius = boundary.height * 0.71f;

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

        boundaryBuffer.setData(boundary);
    }
}
