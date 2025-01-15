package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;
import org.lwjgl.opencl.CL10;

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

        setNewArgs();
    }

    @Override
    protected void setNewArg(KernelDependency KD) {
        CL10.clSetKernelArg(
                KD.targetKernel,
                KD.numberArg,
                nativeBuffer.getByteBuffer()
        );
    }
}