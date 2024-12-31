package org.example.BufferControl;

import static org.example.GLOBAL_STATE.*;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GlobalFloatBuffer extends GlobalBuffer<float[], FloatBuffer> {
    public GlobalFloatBuffer(float[] hostBuffer, MemoryAccessControl memoryAccessControl, boolean isDynamic) {
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

    @Override
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

        checkClBuffer();

        setNewArgs();
    }

    @Override
    protected void addToEnd(float[] data) {
        if (length + data.length < capacity) {
            CL10.clEnqueueWriteBuffer(openClContext.commandQueue, clBuffer, true, (int) length * Float.BYTES, data, null, null);
            length += data.length;

            checkClBuffer();

            setNewArgs();
        } else {
            readBuffer();
            int newLength = data.length + hostBuffer.length;
            capacity = (int) ((newLength > capacity * 1.5) ?
                    newLength * 2 : capacity * 1.5);

            float[] tempBuffer = new float[capacity];

            System.arraycopy(hostBuffer, 0, tempBuffer, 0, length);
            System.arraycopy(data, 0, tempBuffer, length, data.length);

            hostBuffer = tempBuffer;
            reWrightBuffers(true);
        }
    }

    @Override
    public void update(float[] newData) {
        boolean isNewBuffer = false;

        if (!isDynamic && length != newData.length) {
            throw new IllegalStateException("A static buffer cannot be modified.");
        }

        if (newData.length > capacity) {
            capacity = (int) ((newData.length > capacity * 1.5) ?
                    newData.length * 2 : capacity * 1.5);
            hostBuffer = new float[capacity];
            isNewBuffer = true;
        }

        length = newData.length;

        System.arraycopy(newData, 0, hostBuffer, 0, length);

        reWrightBuffers(isNewBuffer);
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
