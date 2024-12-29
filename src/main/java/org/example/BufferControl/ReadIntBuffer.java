package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class ReadIntBuffer  extends BufferContext<int[], IntBuffer>{
    ReadIntBuffer(int[] hostBuffer, MemoryAccessControl memoryAccessControl) {
        this.hostBuffer = hostBuffer;
        this.memoryAccessControl = memoryAccessControl;

        CL10.clCreateBuffer(openClContext.context,  CL10.CL_MEM_WRITE_ONLY, 256, null);

        reWrightBuffers(true);
    }

    public void reWrightBuffers (boolean isNewBuffers) {
        if (nativeBuffer != null) {
            MemoryUtil.memFree(nativeBuffer);
        }
        nativeBuffer = MemoryUtil.memAllocInt(hostBuffer.length);

        setNewArgs();
    }

    @Override
    public void update(int[] newHostBuffer) {
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

    @Override
    public void setNewArg(KernelDependency KD) {
        CL10.clCreateBuffer(openClContext.context,  CL10.CL_MEM_WRITE_ONLY, hostBuffer.length, null);
    }
}
