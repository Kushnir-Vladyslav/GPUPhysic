package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class LocalBuffer extends BufferContext<int[], IntBuffer>{

    public LocalBuffer(int sizeOfBuffer) {
        length = sizeOfBuffer;

        nativeBuffer = MemoryUtil.memAllocInt(1);

        nativeBuffer.put(0, length);

        setNewArgs();
    }

    @Override
    public void update(int[] newSize) {
        if (hostBuffer.length != 1) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array.");
        }

        if (length == newSize[0]) {
            return;
        }

        length = newSize[0];

        nativeBuffer.put(0, length);

        setNewArgs();
    }

    @Override
    public void update() {
    }

    @Override
    public void resize (int newSize) {
        length = newSize;

        nativeBuffer.put(0, length);

        setNewArgs();
    }

    @Override
    public void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(KD.targetKernel.getKernel(), KD.numberArg, nativeBuffer.rewind());
    }

    @Override
    public int[] getData() {
        throw new IllegalStateException("Data from local memory cannot be read.");
    }

    @Override
    public void readBuffer() {
        throw new IllegalStateException("Data from local memory cannot be read.");
    }
}

