package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class ReadFloatBuffer extends ReadBuffer<float[], FloatBuffer>{

    public ReadFloatBuffer(int sizeOfBuffer, MemoryAccessControl memoryAccessControl) {
        this.memoryAccessControl = memoryAccessControl;
        length = sizeOfBuffer;

        newBuffers();
    }

    @Override
    protected void newBuffers () {
        hostBuffer = new float[length];
        nativeBuffer = MemoryUtil.memAllocFloat(length);
        clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(),
                length, null);

        checkClBuffer();

        setNewArgs();
    }
}
