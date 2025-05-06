package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Readable;
import com.jopencl.core.memory.buffer.Writable;

public class GlobalDynamicWrightOnlyBuffer
        extends GlobalDynamicBuffer
        implements Writable<GlobalDynamicWrightOnlyBuffer> {

    public GlobalDynamicWrightOnlyBuffer () {
        setWritable(true);
    }
}
