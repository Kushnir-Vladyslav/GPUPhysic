package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.buffer.AdditionalInitiation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;
import org.lwjgl.system.MemoryUtil;

public abstract class GlobalBuffer
        extends     AbstractBuffer
        implements AdditionalInitiation<GlobalBuffer> {

    private PointerBuffer transmitter;

    public GlobalBuffer () {
        if (transmitter == null) {
            transmitter = MemoryUtil.memAllocPointer(1);
        }

        this.setFlags(CL10.CL_MEM_READ_WRITE);
    }

    @Override
    public void addInit() {
        if (clBuffer == 0) {
            clBuffer = createClBuffer();
        }
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
