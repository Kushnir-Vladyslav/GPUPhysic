package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.VECTOR_SIZE;
import static org.example.GLOBAL_STATE.openClContext;

public class ReadIntBuffer extends BufferContext<int[], IntBuffer>{

    public ReadIntBuffer(int sizeOfBuffer, MemoryAccessControl memoryAccessControl) {
        this.memoryAccessControl = memoryAccessControl;
        length = sizeOfBuffer;

        newBuffers();
    }

    protected void newBuffers () {
        hostBuffer = new int[length];
        nativeBuffer = MemoryUtil.memAllocInt(length);
        clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(),
                length, null);

        checkClBuffer();

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

        newBuffers();
    }

    @Override
    public void update() {
    }

    @Override
    public void resize (int newSize) {
        if (length == newSize) {
            return;
        }
        length = newSize;

        newBuffers();
    }
}
