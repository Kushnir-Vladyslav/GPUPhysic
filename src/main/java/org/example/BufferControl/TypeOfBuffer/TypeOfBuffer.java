package org.example.BufferControl.TypeOfBuffer;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public abstract class TypeOfBuffer {
    protected ByteBuffer buffer;
    protected Object array;

    protected void create (int length){
        if (buffer != null){
            MemoryUtil.memFree(buffer);
        }
        buffer = MemoryUtil.memAlloc(length * getByteSize());
    }

    public void set (Object arr) {
        set(arr, 0);
    }

    public abstract void set (Object arr, int startPosition);

    public abstract int getSize(Object arr);

    public abstract int getByteSize();

    public abstract Object getArr();

    public long createClBuffer(long flags) {
        return CL10.clCreateBuffer(
                openClContext.context,
                flags,
                buffer.capacity(),
                null
        );
    }

    public void rewriteClBuffer(long clBuffer){
        rewriteClBuffer(clBuffer, 0);
    }

    public void rewriteClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueWriteBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * getByteSize(),
                buffer,
                null,
                null
        );
    }

    public void readClBuffer(long clBuffer) {
        readClBuffer(clBuffer, 0);
    }

    public void readClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueReadBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * getByteSize(),
                buffer,
                null,
                null
        );
    }

    public void reSize (int newLength) {
        destroy();
        create(newLength);
    }

    public void destroy (){
        if (buffer != null) {
            MemoryUtil.memFree(buffer);
            buffer = null;
        }
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }
}
