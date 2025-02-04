package org.example.Structs;

import org.example.BufferControl.GlobalStaticBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.lwjgl.opencl.CL10;

import static org.example.GLOBAL_STATE.bufferManager;
import static org.example.GLOBAL_STATE.WorkZoneHeight;
import static org.example.GLOBAL_STATE.WorkZoneWidth;

public class Canvas {

    GlobalStaticBuffer<IntBufferType> canvas;

    public Canvas () {
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
