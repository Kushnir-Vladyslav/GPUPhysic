package org.example.BufferControl;

import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SingleValueBuffer<K extends TypeOfBuffer> extends BufferContext<K> {
    ByteBuffer byteBuffer;

    public SingleValueBuffer(Class<K> type) {
        super(type);
    }

    public void init(Object object) {
        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(1);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        byteBuffer = ByteBuffer.
                allocateDirect(nativeBuffer.getByteSize()).
                order(ByteOrder.nativeOrder());

        setData(object);
    }

    public void setData(Object object) {
        nativeBuffer.set(object);
    }

    @Override
    protected void setNewArg(KernelDependency KD) {
        nativeBuffer.getByteBuffer(byteBuffer);

        CL10.clSetKernelArg(
                KD.targetKernel,
                KD.numberArg,
                byteBuffer
        );
    }

    @Override
    public void destroy() {
        if (byteBuffer != null) {
            MemoryUtil.memFree(byteBuffer);
            byteBuffer = null;
        }

        super.destroy();
    }
}