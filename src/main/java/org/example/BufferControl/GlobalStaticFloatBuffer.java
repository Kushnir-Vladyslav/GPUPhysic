package org.example.BufferControl;

import static org.example.GLOBAL_STATE.*;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GlobalStaticFloatBuffer extends BufferContext<float[], FloatBuffer>{
    GlobalStaticFloatBuffer(float[] hostBuffer, MemoryAccessControl memoryAccessControl) {
        this.hostBuffer = hostBuffer;
        this.memoryAccessControl = memoryAccessControl;

        nativeBuffer = MemoryUtil.memAllocFloat(hostBuffer.length);
        for (float element : hostBuffer) {
            nativeBuffer.put(element);
        }
        nativeBuffer.rewind();

        clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(), nativeBuffer, null);
        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }

        setNewArgs();
    }

    @Override
    public void update(float[] newHostBuffer) {
        if (newHostBuffer != null) {
            if (newHostBuffer.length != hostBuffer.length) {
                throw new IllegalArgumentException("The length of the new array does not match the length of the existing buffer.");
            }
            hostBuffer = newHostBuffer;
        }

        nativeBuffer.rewind();
        for (float element : hostBuffer) {
            nativeBuffer.put(element);
        }
        nativeBuffer.rewind();

        CL10.clEnqueueWriteBuffer(openClContext.commandQueue, clBuffer, true, 0, nativeBuffer, null, null);

        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }

        setNewArgs();
    }

    @Override
    public void resize (int newSize) {
        throw new IllegalStateException("Program execution path is incorrect.");
    }
}
