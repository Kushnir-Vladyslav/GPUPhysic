package com.jopencl.core.memory.buffer.typedBuffers.GlobalBuffer;

import com.jopencl.core.memory.buffer.Dynamical;
import com.jopencl.core.memory.buffer.AbstractGlobalBuffer;

public class GlobalDynamicBuffer extends AbstractGlobalBuffer implements Dynamical<GlobalDynamicBuffer> {
    public GlobalDynamicBuffer () {
        setDynamic(true);
        setInitSize(10);
    }

}
