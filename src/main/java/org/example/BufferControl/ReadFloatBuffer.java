package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class ReadFloatBuffer extends BufferContext<float[], FloatBuffer>{

    ReadFloatBuffer(int sizeOfBuffer, MemoryAccessControl memoryAccessControl) {
        this.memoryAccessControl = memoryAccessControl;
        length = sizeOfBuffer;

        newBuffers();
    }

    protected void newBuffers () {
        hostBuffer = new float[length];
        nativeBuffer = MemoryUtil.memAllocFloat(length);
        clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(),
                length, null);

        setNewArgs();
    }

    @Override
    public void update(float[] newSize) {
        if (hostBuffer.length != 1) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array.");
        }
        if (length == newSize[0]) {
            return;
        }
        length = (int) newSize[0];

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
