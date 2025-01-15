package org.example.BufferControl;


import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.lwjgl.opencl.CL10;

public class LocalBuffer<K extends TypeOfBuffer> extends BufferContext<K> {
    protected long size;

    public LocalBuffer(Class<K> type) {
        super(type);
    }

    public void init(long length) {
        try {
            nativeBuffer = type.getConstructor(int.class).newInstance(0);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        size = length * nativeBuffer.getByteSize();
    }

    public void resize(long newSize) {
        size = newSize * nativeBuffer.getByteSize();
    }

    @Override
    protected void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(KD.targetKernel, KD.numberArg, size);
    }
}
