package com.jopencl.core.memory.buffer.typedBuffers.GlobalBuffer;

import com.jopencl.core.memory.buffer.*;
import com.jopencl.core.memory.data.Data;
import com.jopencl.util.OpenClContext;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

public abstract class GlobalBuffer
        extends KernelAwareBuffer {

    private PointerBuffer transmitter;

    public GlobalBuffer () {
        setFlags(CL10.CL_MEM_READ_WRITE);
    }

    @Override
    public void addInit() {
        super.addInit();

        transmitter = MemoryUtil.memAllocPointer(1);

        if (clBuffer == 0) {
            clBuffer = createClBuffer();
        }
    }

    public void setup (Class<Data> clazz,
                       OpenClContext context,
                       boolean copyNativeBuffer,
                       boolean copyHostBuffer,
                       int initSize) {
        setDataClass(clazz);
        setOpenClContext(context);
        setCopyNativeBuffer(copyNativeBuffer);
        setCopyHostBuffer(copyHostBuffer);
        setInitSize(initSize);
    }

    public void setup (String bufferName,
                       Class<Data> clazz,
                       OpenClContext context,
                       boolean copyNativeBuffer,
                       boolean copyHostBuffer,
                       int initSize) {
        setBufferName(bufferName);
        setDataClass(clazz);
        setOpenClContext(context);
        setCopyNativeBuffer(copyNativeBuffer);
        setCopyHostBuffer(copyHostBuffer);
        setInitSize(initSize);
    }

    @Override
    protected void setKernelArg (long targetKernel, int argIndex) {
        CL10.clSetKernelArg(
                targetKernel,
                argIndex,
                transmitter.put(0, clBuffer).rewind()
        );
    }

    @Override
    public void destroy () {
        super.destroy();

        if (transmitter != null) {
            MemoryUtil.memFree(transmitter);
            transmitter = null;
        }
    }
}
