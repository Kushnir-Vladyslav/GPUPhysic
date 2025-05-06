package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.Readable;

public class GlobalDynamicReadOnlyBuffer
        extends GlobalDynamicBuffer
        implements Readable <GlobalDynamicReadOnlyBuffer> {

    public GlobalDynamicReadOnlyBuffer () {
        setReadable(true);
    }
}
