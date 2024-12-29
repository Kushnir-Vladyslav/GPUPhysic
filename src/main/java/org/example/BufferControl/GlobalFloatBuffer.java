package org.example.BufferControl;

import static org.example.GLOBAL_STATE.*;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GlobalFloatBuffer extends BufferContext<float[], FloatBuffer> {
    protected int capacity;
    protected final boolean isDynamic;

    GlobalFloatBuffer(float[] hostBuffer, MemoryAccessControl memoryAccessControl, boolean isDynamic) {
        this.memoryAccessControl = memoryAccessControl;
        this.isDynamic = isDynamic;
       length = hostBuffer.length;
        if (isDynamic) {
            capacity = 2 * length;
            this.hostBuffer = new float[capacity];
            System.arraycopy(hostBuffer, 0, this.hostBuffer, 0, length);
        } else {
            capacity = length;
            this.hostBuffer = hostBuffer;
        }

        reWrightBuffers(true);
    }

    protected void reWrightBuffers (boolean isNewBuffers) {
        if (isNewBuffers) {
            if (nativeBuffer != null) {
                MemoryUtil.memFree(nativeBuffer);
            }
            nativeBuffer = MemoryUtil.memAllocFloat(capacity);
        } else {
            nativeBuffer.rewind();
        }
        nativeBuffer.put(hostBuffer);
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
    public void update(float[] newDats) {
        boolean isNewBuffer = false;

        if (!isDynamic && length != newDats.length) {
            throw new IllegalStateException("A static buffer cannot be modified.");
        }

        if (newDats.length > capacity) {
            capacity = (int) ((newDats.length > capacity * 1.5) ?
                    newDats.length * 2 : capacity * 1.5);
            hostBuffer = new float[capacity];
            isNewBuffer = true;
        }

        length = newDats.length;

        System.arraycopy(newDats, 0, hostBuffer, 0, length);

        reWrightBuffers(isNewBuffer);
    }

    @Override
    public void update() {
        reWrightBuffers(false);
    }

    @Override
    public void resize (int newSize) {
        if (!isDynamic) {
            throw new IllegalStateException("A static buffer cannot be modified.");
        }
        if (newSize < length) {
            throw new IllegalArgumentException("The new capacity is less than the existing amount of data.");
        }
        if (newSize == capacity) {
            return;
        }

        capacity = newSize;

        float[] tmpFloat = new float[capacity];

        System.arraycopy(hostBuffer, 0, tmpFloat, 0, length);

        hostBuffer = tmpFloat;

        reWrightBuffers(true);
    }
}
