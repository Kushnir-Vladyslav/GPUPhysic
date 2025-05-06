package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Readable;
import com.jopencl.core.memory.buffer.Writable;

public class GlobalDynamicReadWrightBuffer
        extends GlobalDynamicBuffer
        implements Readable <GlobalDynamicReadWrightBuffer>,
        Writable <GlobalDynamicReadWrightBuffer> {

    public GlobalDynamicReadWrightBuffer () {
        setReadable(true);
        setWritable(true);
    }
}
