package org.example.Kernel;

import org.example.BufferControl.BufferContext;
import org.example.BufferControl.GlobalFloatBuffer;
import org.example.BufferControl.MemoryAccessControl;
import org.example.Kernel.Kernel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.example.GLOBAL_STATE.*;
import static org.example.OpenCL.*;


public class BoundaryCollisionKernel extends Kernel {
    public FloatBuffer cursorBuffer;

    public Long clWorkZoneBoundaryBuffer;
    public Long clCursorPosition;

    public Long kernelBoundaryCollision;

    PointerBuffer global;
    PointerBuffer local;

    float[] boundary;
    BufferContext<?,?> boundaryBuffer;

    BoundaryCollisionKernel () {
        createKernel("BoundaryCollision");


        if (!bufferManager.isExist("Particles")) {
            bufferManager.set("Particles",
                    new GlobalFloatBuffer());
        }
        //створення буферів що містять інформацію про границі робочиї зони
        boundary = new float[5];
        boundary[0] = WorkZoneWidth;
        boundary[1] = WorkZoneHeight;
        boundary[2] = (float) Math.sqrt(WorkZoneWidth * WorkZoneWidth + WorkZoneHeight * WorkZoneHeight) / 2;
        boundary[3] = (float) WorkZoneWidth / 2;
        boundary[4] = (float) WorkZoneHeight - boundary[2];
        if (!bufferManager.isExist("Boundary")) {
            bufferManager.set("Boundary",
                    new GlobalFloatBuffer(boundary, MemoryAccessControl.HOST_W_DEVICE_R, false));
        }
        boundaryBuffer = bufferManager.get("Boundary");
        boundaryBuffer.addKernel(this, 1);

        //створення буферу що буде місти положення курсору
        cursorBuffer = MemoryUtil.memAllocFloat(3);
        setCursorBuffer();
        createRHostBuffer(cursorBuffer, clCursorPosition);

        createKernel("BoundaryCollision");

        global = MemoryUtil.memAllocPointer(1);
        local = MemoryUtil.memAllocPointer(1);
    }

    //встановленя параметрів курсору при натисканні/відпусканні
    public void setCursorBuffer () {
        cursorBuffer.rewind();
        cursorBuffer.put(cursorPosition.radius).put(cursorPosition.x).put(cursorPosition.y).rewind();
    }

    //ввстановлення парамтрів що передадуться ядру
    public void setArgumentsForBoundaryCollisionKernel (){
        CL10.clSetKernelArg(kernelBoundaryCollision, 0, PointerBuffer.allocateDirect(1).put(0, clParticlesBuffer));
        CL10.clSetKernelArg(kernelBoundaryCollision, 1, PointerBuffer.allocateDirect(1).put(0, clWorkZoneBoundaryBuffer));
        CL10.clSetKernelArg(kernelBoundaryCollision, 2, PointerBuffer.allocateDirect(1).put(0, clCursorPosition));
    }

    @Override
    public void run () {
        local.put(0, LOCAL_WORK_SIZE).rewind();
        global.put(0, (long) Math.ceil(particles.length / (float) LOCAL_WORK_SIZE) * LOCAL_WORK_SIZE).rewind();

        CL10.clEnqueueNDRangeKernel(
                commandQueue, kernelBoundaryCollision, 1, null,
                global, local,
                null, null
        );
    }

    @Override
    public void destroy() {
        //звільнення ядра
        CL10.clReleaseKernel(kernelBoundaryCollision);

        //звільгнення відео памяті
        CL10.clReleaseMemObject(clWorkZoneBoundaryBuffer);
        CL10.clReleaseMemObject(clCursorPosition);

        //звільнення памяті виділеної в купі
        MemoryUtil.memFree(cursorBuffer);
        MemoryUtil.memFree(global);
        MemoryUtil.memFree(local);
    }
}
