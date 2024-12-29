package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class LocalBuffer extends BufferContext<int[], IntBuffer>{

    LocalBuffer(int[] hostBuffer, MemoryAccessControl memoryAccessControl) {
        if (hostBuffer.length != 1 || memoryAccessControl != null) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array. No flags are required.");
        }

        nativeBuffer = MemoryUtil.memAllocInt(1);
        this.hostBuffer = hostBuffer;

        nativeBuffer.put(0, hostBuffer[0]);

        setNewArgs();
    }

    @Override
    public void update(int[] newHostBuffer) {
        if (hostBuffer.length != 1) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array.");
        }

        hostBuffer = newHostBuffer;
        nativeBuffer.put(0, hostBuffer[0]);

        setNewArgs();
    }

    @Override
    public void resize (int newSize) {
        hostBuffer[0] = newSize;
        nativeBuffer.put(0, hostBuffer[0]);

        setNewArgs();
    }

    @Override
    public void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(KD.targetKernel.getKernel(), KD.numberArg, nativeBuffer.rewind());

    }
}

