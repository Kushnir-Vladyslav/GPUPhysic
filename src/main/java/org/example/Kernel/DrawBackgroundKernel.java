package org.example.Kernel;

import org.example.BufferControl.GlobalStaticBuffer;
import org.example.BufferControl.SingleValueBuffer;
import org.example.BufferControl.TypeOfBuffer.BoundaryBuffer;
import org.example.BufferControl.TypeOfBuffer.CursorPositionBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;
import org.example.Structs.Boundary;
import org.example.Structs.Canvas;
import org.example.Structs.CursorPosition;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import static org.example.GLOBAL_STATE.*;


public class DrawBackgroundKernel extends Kernel {

    final int LOCAL_WORK_SIZE = 16;

    GlobalStaticBuffer<IntBufferType> canvasBuffer;
    SingleValueBuffer<BoundaryBuffer> boundaryBuffer;
    SingleValueBuffer<CursorPositionBuffer> cursorPositionBuffer;

    public DrawBackgroundKernel() {
        createKernel("DrawBackground", "Structs", "Math");

        if (!bufferManager.isExist("CanvasBuffer")) {
            if (canvas == null) {
                canvas = new Canvas();
            } else {
                canvas.update();
            }
        }

        if (!bufferManager.isExist("BoundaryBuffer")) {
            if (boundary == null) {
                boundary = new Boundary();
            } else {
                boundary.update();
            }
        }

        if (!bufferManager.isExist("CursorBuffer")) {
            if (cursorPosition == null) {
                cursorPosition = new CursorPosition();
            } else {
                cursorPosition.update();
            }
        }

        canvasBuffer = bufferManager.getBuffer("CanvasBuffer", GlobalStaticBuffer.class, IntBufferType.class);
        boundaryBuffer = bufferManager.getBuffer("BoundaryBuffer", SingleValueBuffer.class, BoundaryBuffer.class);
        cursorPositionBuffer = bufferManager.getBuffer("CursorBuffer", SingleValueBuffer.class, CursorPositionBuffer.class);

        canvasBuffer.addKernel(kernel, 0);
        boundaryBuffer.addKernel(kernel, 1);
        cursorPositionBuffer.addKernel(kernel, 2);

        global = MemoryUtil.memAllocPointer(2).
                put((long) Math.ceil(WorkZoneWidth / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE).
                put((long) Math.ceil(WorkZoneHeight / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE);
        local = MemoryUtil.memAllocPointer(2).put(LOCAL_WORK_SIZE).put(LOCAL_WORK_SIZE);
    }

    @Override
    public void run() {
        err = CL10.clEnqueueNDRangeKernel(
                openClContext.commandQueue, kernel, 2, null,
                global.rewind(), local.rewind(),
                null, null
        );
        checkError();
        CL10.clFinish(openClContext.commandQueue);
    }
}
