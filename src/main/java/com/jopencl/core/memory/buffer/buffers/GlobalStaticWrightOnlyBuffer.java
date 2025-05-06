package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Writable;

public class GlobalStaticWrightOnlyBuffer
        extends GlobalStaticBuffer implements Writable<GlobalStaticWrightOnlyBuffer> {

    public GlobalStaticWrightOnlyBuffer () {
        setWritable(true);
    }
}
