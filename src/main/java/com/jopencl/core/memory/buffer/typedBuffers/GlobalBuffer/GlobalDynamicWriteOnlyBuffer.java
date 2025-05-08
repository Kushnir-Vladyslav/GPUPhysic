package com.jopencl.core.memory.buffer.typedBuffers.GlobalBuffer;

import com.jopencl.core.memory.buffer.Writable;

public class GlobalDynamicWriteOnlyBuffer
        extends GlobalDynamicBuffer
        implements Writable<GlobalDynamicWriteOnlyBuffer> {

    public GlobalDynamicWriteOnlyBuffer() {
        setWritable(true);
    }
}
