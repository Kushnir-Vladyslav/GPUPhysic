package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SingleValueBuffer<K extends TypeOfBuffer> extends BufferContext<K> {

    public SingleValueBuffer(Class<K> type) {
        super(type);
    }

    public void init(Object object) {
        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(1);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        setData(object);
    }

    public void setData(Object object) {
        nativeBuffer.set(object);
    }

    @Override
    protected void setNewArg(KernelDependency KD) {

        ByteBuffer byteBuffer = nativeBuffer.getByteBuffer();

        if (byteBuffer == null) {
            throw new IllegalStateException("The getByteBuffer method is not implemented.");
        }
        CL10.clSetKernelArg(
                KD.targetKernel,
                KD.numberArg,
                byteBuffer
        );
        MemoryUtil.memFree(byteBuffer);
    }
}