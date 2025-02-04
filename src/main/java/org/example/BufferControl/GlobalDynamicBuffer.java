package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.example.Structs.Particle;
import org.lwjgl.opencl.CL10;

public class GlobalDynamicBuffer<K extends TypeOfBuffer> extends BufferContext <K>{
    protected int size;
    protected int capacity;
    protected long flags;

    public GlobalDynamicBuffer(Class<K> type) {
        super(type);
    }

    public void init (int length, long flags) {
        capacity = length;
        size = 0;
        this.flags = flags;

        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(capacity);
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
        if (sizeOfArr + size >= capacity) {
            capacity = (int) ((sizeOfArr + size > capacity * 1.5) ?
                    sizeOfArr * 1.5 : capacity * 1.5);

            nativeBuffer.readClBuffer(clBuffer);
            Object oldArr = nativeBuffer.getArr();
            nativeBuffer.reSize(capacity);
            nativeBuffer.set(oldArr);
            nativeBuffer.set(arr, size);

            if (clBuffer != 0) {
                CL10.clReleaseMemObject(clBuffer);
            }

            clBuffer = nativeBuffer.createClBuffer(flags);
            checkClBuffer();
            setNewArgs();
            nativeBuffer.rewriteClBuffer(clBuffer);
        } else {
            K tempBuffer;
            try {
                tempBuffer = type.getConstructor(int.class).newInstance(sizeOfArr);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            tempBuffer.set(arr);
            tempBuffer.rewriteClBuffer(clBuffer, size);
            tempBuffer.destroy();
        }
        size += sizeOfArr;
    }

    //змінює розмір на вказаний, всі данні в буфері будуть втрачені
    public void reduceTo (int newSize) {
        capacity = newSize;
        size = 0;
        nativeBuffer.reSize(capacity);

        if (clBuffer != 0) {
            CL10.clReleaseMemObject(clBuffer);
        }

        clBuffer = nativeBuffer.createClBuffer(flags);
        checkClBuffer();
        setNewArgs();
    }

    public void increaseTo (int newSize) {
        if (newSize >= capacity) {
            capacity = (int) ((newSize  > capacity * 1.5) ?
                    newSize * 1.5 : capacity * 1.5);

            nativeBuffer.readClBuffer(clBuffer);
            Object oldArr = nativeBuffer.getArr();
            nativeBuffer.reSize(capacity);
            nativeBuffer.set(oldArr);

            if (clBuffer != 0) {
                CL10.clReleaseMemObject(clBuffer);
            }

            clBuffer = nativeBuffer.createClBuffer(flags);
            checkClBuffer();
            setNewArgs();
            nativeBuffer.rewriteClBuffer(clBuffer);
        }
    }

    public Object readData() {
        nativeBuffer.readClBuffer(clBuffer);
        return nativeBuffer.getArr();
    }
}
