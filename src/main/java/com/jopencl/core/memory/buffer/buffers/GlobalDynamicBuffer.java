package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Dynamical;

public class GlobalDynamicBuffer extends GlobalBuffer implements Dynamical<GlobalDynamicBuffer> {
    public GlobalDynamicBuffer () {
        this.setDynamic(true);
    }

}
