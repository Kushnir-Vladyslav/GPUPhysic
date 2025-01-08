package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.example.GLOBAL_STATE.openClContext;

public class FloatBufferType extends TypeOfBuffer<FloatBuffer> {

    public FloatBufferType(int length) {
        create(length);
    }

    @Override
    protected void create(int length) {
        buffer = MemoryUtil.memAllocFloat(length);
    }

    @Override
    public void set(Object arr) {
        if (!float[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        float[] castedArr = (float[]) arr;
        buffer.rewind().put(castedArr).rewind();
    }

    @Override
    public void set(Object arr, int startPosition) {
        if (!float[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        float[] castedArr = (float[]) arr;
        buffer.put(startPosition, castedArr).rewind();
    }

    @Override
    public int getSize(Object arr) {
        if (!float[].class.isInstance(arr) || buffer == null) {
            throw new IllegalArgumentException("Invalid array type, or not initialized.");
        }
        float[] castedArr = (float[]) arr;
        return castedArr.length;
    }

    @Override
    public int getByteSize() {
        return Float.BYTES;
    }

    @Override
    public Object getArr(int length) {
        float[] arr = new float[length];
        buffer.get(arr);
        return arr;
    }

    @Override
    public long createClBuffer(long flags) {
        return CL10.clCreateBuffer(
                openClContext.context,
                flags,
                buffer.capacity() * Float.BYTES,
                null
        );
    }

    @Override
    public void rewriteClBuffer(long clBuffer, long offset) {
        CL10.clEnqueueWriteBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset * Float.BYTES,
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
                offset * Float.BYTES,
                buffer,
                null,
                null
        );
    }

    @Override
    public void getByteBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear().putFloat(buffer.get(0)).rewind();
    }
}
