package com.jopencl.core.memory.buffer.typedBuffers.GlobalBuffer;

import com.jopencl.core.memory.buffer.Dynamical;

public class GlobalDynamicBuffer extends GlobalBuffer implements Dynamical<GlobalDynamicBuffer> {
    public GlobalDynamicBuffer () {
        setDynamic(true);
        setInitSize(10);
    }

}
