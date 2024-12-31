package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class ReadIntBuffer extends ReadBuffer<int[], IntBuffer>{

    public ReadIntBuffer(int sizeOfBuffer, MemoryAccessControl memoryAccessControl) {
        this.memoryAccessControl = memoryAccessControl;
        length = sizeOfBuffer;

        newBuffers();
    }

    @Override
    protected void newBuffers () {
        hostBuffer = new int[length];
        nativeBuffer = MemoryUtil.memAllocInt(length);
        clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(),
                length, null);

        checkClBuffer();

        setNewArgs();
    }
}
