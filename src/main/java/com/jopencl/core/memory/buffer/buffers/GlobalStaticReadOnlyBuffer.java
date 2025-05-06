package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Readable;

public class GlobalStaticReadOnlyBuffer
        extends GlobalStaticBuffer
        implements Readable<GlobalStaticReadOnlyBuffer> {

    public GlobalStaticReadOnlyBuffer () {
        setReadable(true);
    }
}
