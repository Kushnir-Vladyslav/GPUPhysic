package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.lwjgl.opencl.CL10;

public class GlobalDynamicBuffer<K extends TypeOfBuffer> extends BufferContext <K>{
    protected int size;
    protected int capacity;
    protected long flags;

    public GlobalDynamicBuffer(Class<K> type) {
        super(type);
    }

    public void init (int length, long flags) {
        capacity = (int) (length * 1.5);
        size = length;
        this.flags = flags;

        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(length);
        } catch (Exception e) {
            throw new  IllegalStateException(e);
        }

        clBuffer = nativeBuffer.createClBuffer(flags);

        checkClBuffer();
    }

    public void setData(Object arr) {
        int sizeOfArr = nativeBuffer.getSize(arr);
        if (sizeOfArr > capacity) {
            capacity = (int) ((sizeOfArr > capacity * 1.5) ?
                    sizeOfArr * 1.5 : capacity * 1.5);

            nativeBuffer.reSize(capacity);

            if (clBuffer != 0) {
                CL10.clReleaseMemObject(clBuffer);
            }

            clBuffer = nativeBuffer.createClBuffer(flags);
        }
        nativeBuffer.set(arr);
        nativeBuffer.rewriteClBuffer(clBuffer, 0);
        size = sizeOfArr;
        checkClBuffer();
        setNewArgs();
    }

    public void addData(Object arr) {
        int sizeOfArr = nativeBuffer.getSize(arr);
        if (sizeOfArr + size > capacity) {
            capacity = (int) ((sizeOfArr + size > capacity * 1.5) ?
                    sizeOfArr * 1.5 : capacity * 1.5);

            nativeBuffer.readClBuffer(clBuffer, 0);
            Object oldArr = nativeBuffer.getArr();
            nativeBuffer.reSize(capacity);
            nativeBuffer.set(oldArr);
            nativeBuffer.set(arr, size);

            if (clBuffer != 0) {
                CL10.clReleaseMemObject(clBuffer);
            }

            clBuffer = nativeBuffer.createClBuffer(flags);
            nativeBuffer.rewriteClBuffer(clBuffer, 0);
        } else {
            K tempBuffer;
            try {
                tempBuffer = type.getConstructor(int.class).newInstance(sizeOfArr);
            } catch (Exception e) {
                throw new  IllegalStateException(e);
            }
            tempBuffer.set(arr);
            tempBuffer.rewriteClBuffer(clBuffer, size);
            tempBuffer.destroy();
        }
        size += sizeOfArr;
        checkClBuffer();
        setNewArgs();
    }

    public Object readData() {
        nativeBuffer.readClBuffer(clBuffer, 0);
        return nativeBuffer.getArr();
    }
}
