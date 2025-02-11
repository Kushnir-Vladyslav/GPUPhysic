package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.GlobalStaticBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.lwjgl.opencl.CL10;


public class Canvas {
    private static Canvas canvasInstance;

    BufferManager bufferManager;

    private GlobalStaticBuffer<IntBufferType> canvas;

    private int canvasWidth;
    private int canvasHeight;

    public static Canvas getInstance() {
        if (canvasInstance == null) {
            canvasInstance = new Canvas();
        }

        return canvasInstance;
    }

    private Canvas () {
        bufferManager = BufferManager.getInstance();

        canvasWidth = 256 * 4;
        canvasHeight = 256 * 3;

        update();
    }

    public static int getCanvasWidth() {
        return getInstance().canvasWidth;
    }

    public static int getCanvasHeight() {
        return getInstance().canvasHeight;
    }

    public void update () {
        if (bufferManager.isExist("CanvasBuffer")) {
            canvas = bufferManager.getBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
        } else {
            canvas = bufferManager.createBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
            canvas.init(canvasHeight * canvasWidth, CL10.CL_MEM_WRITE_ONLY);
        }
    }

    public int[] getCanvas () {
        return (int[]) canvas.readData();
    }
}
