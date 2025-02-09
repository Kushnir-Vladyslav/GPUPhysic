package org.example.Structs;

import org.example.BufferControl.BufferManager;
import org.example.BufferControl.GlobalStaticBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.lwjgl.opencl.CL10;

import static org.example.JavaFX.GLOBAL_STATE.WorkZoneHeight;
import static org.example.JavaFX.GLOBAL_STATE.WorkZoneWidth;

public class Canvas {
    BufferManager bufferManager;

    GlobalStaticBuffer<IntBufferType> canvas;

    public Canvas () {
        bufferManager = BufferManager.getInstance();

        update();
    }

    public void update () {
        if (bufferManager.isExist("CanvasBuffer")) {
            canvas = bufferManager.getBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
        } else {
            canvas = bufferManager.createBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
            canvas.init(WorkZoneHeight * WorkZoneWidth, CL10.CL_MEM_WRITE_ONLY);
        }
    }

    public int[] getCanvas () {
        return (int[]) canvas.readData();
    }
}
