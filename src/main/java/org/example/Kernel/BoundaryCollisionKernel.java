package org.example.Kernel;

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

    BoundaryCollisionKernel () {
        //створення буферів що містять інформацію про зраниці робочиї зони
        float radiusBoundaryCircle = (float) Math.sqrt(WorkZoneWidth * WorkZoneWidth + WorkZoneHeight * WorkZoneHeight) / 2;
        FloatBuffer boundaryBuffer = MemoryUtil.memAllocFloat(5)
                .put(WorkZoneWidth).put(WorkZoneHeight)
                .put(radiusBoundaryCircle)
                .put((float) WorkZoneWidth / 2).put((float) WorkZoneHeight - radiusBoundaryCircle).rewind();
        createRHostBuffer(boundaryBuffer, clWorkZoneBoundaryBuffer);
        MemoryUtil.memFree(boundaryBuffer);

        //створення буферу що буде місти положення курсору
        cursorBuffer = MemoryUtil.memAllocFloat(3);
        setCursorBuffer();
        createRHostBuffer(cursorBuffer, clCursorPosition);

        createKernel("BoundaryCollision.cl", kernelBoundaryCollision);

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
