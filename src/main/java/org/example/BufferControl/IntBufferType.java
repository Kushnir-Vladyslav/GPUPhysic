package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class IntBufferType extends TypeOfBuffer<IntBuffer> {

    public IntBufferType(int length) {
        create(length);
    }

    @Override
    protected void create(int length) {
        buffer = MemoryUtil.memAllocInt(length);
    }

    @Override
    public void set(Object arr) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = (int[]) arr;
        buffer.rewind().put(castedArr).rewind();
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = int[].class.cast(arr);
        buffer.put(startPosition, castedArr).rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (!int[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        int[] castedArr = (int[]) arr;
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Integer.BYTES;
    }

    @Override
    public Object getArr(int length) {
        int[] arr = new int[length];
        buffer.get(arr);
        return arr;
    }

    @Override
    public long createClBuffer(long flags) {
        return CL10.clCreateBuffer(
                openClContext.context,
                flags,
                buffer.capacity() * Integer.BYTES,
                null
        );
    }

    @Override
    public void rewriteClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueWriteBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * Integer.BYTES,
                buffer,
                null,
                null
        );
    }

    @Override
    public void readClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueReadBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * Integer.BYTES,
                buffer,
                null,
                null
        );
    }

    @Override
    public void getByteBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear().putInt(buffer.get(0)).rewind();
    }
}
