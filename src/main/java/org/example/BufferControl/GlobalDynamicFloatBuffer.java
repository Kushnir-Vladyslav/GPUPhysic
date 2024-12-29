package org.example.BufferControl;

import static org.example.GLOBAL_STATE.*;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GlobalDynamicFloatBuffer extends BufferContext<float[], FloatBuffer> {
    int capacity;
    int length;

    GlobalDynamicFloatBuffer(float[] hostBuffer, MemoryAccessControl memoryAccessControl) {
        this.hostBuffer = hostBuffer;
        this.memoryAccessControl = memoryAccessControl;
        length = hostBuffer.length;
        capacity = 2 * length;

        reWrightBuffers(true);
    }

    public void reWrightBuffers (boolean isNewBuffers) {
        if (isNewBuffers) {
            if (nativeBuffer != null) {
                MemoryUtil.memFree(nativeBuffer);
            }
            nativeBuffer = MemoryUtil.memAllocFloat(capacity);
        } else {
            nativeBuffer.rewind();
        }
        for (float element : hostBuffer) {
            nativeBuffer.put(element);
        }
        nativeBuffer.rewind();

        if (isNewBuffers && clBuffer != 0) {
            CL10.clReleaseMemObject(clBuffer);
        }
        if (isNewBuffers) {
            clBuffer = CL10.clCreateBuffer(openClContext.context, memoryAccessControl.getFlags(), nativeBuffer, null);
        } else {
            CL10.clEnqueueWriteBuffer(openClContext.commandQueue, clBuffer, true, 0, nativeBuffer, null, null);
        }
        if (clBuffer == 0) {
            throw new IllegalStateException("Failed to create OpenCL memory buffers.");
        }

        setNewArgs();
    }

    @Override
    public void update(float[] newHostBuffer) {
        if (newHostBuffer != null) {
            if (newHostBuffer.length > capacity) {
                capacity = (int) ((newHostBuffer.length > capacity * 1.5) ?
                        newHostBuffer.length * 2 : capacity * 1.5);
                length = newHostBuffer.length;
                hostBuffer = newHostBuffer;

                reWrightBuffers(true);

            } else {
                length = newHostBuffer.length;
                hostBuffer = newHostBuffer;

                reWrightBuffers(false);
            }
        } else {
            reWrightBuffers(false);
        }
    }

    @Override
    public void resize (int newSize) {
        if (newSize < length) {
            throw new IllegalArgumentException("The new capacity is less than the existing amount of data.");
        }
        if (newSize == capacity) {
            return;
        }

        capacity = newSize;

        reWrightBuffers(true);
    }
}
