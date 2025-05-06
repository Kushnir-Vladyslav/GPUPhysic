package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Readable;
import com.jopencl.core.memory.buffer.Writable;

public class GlobalStaticReadWrightBuffer
        extends GlobalStaticBuffer
        implements Readable<GlobalStaticReadWrightBuffer>,
        Writable <GlobalStaticReadWrightBuffer> {

    public GlobalStaticReadWrightBuffer () {
        setReadable(true);
        setWritable(true);
    }
}
