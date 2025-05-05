package com.jopencl.core.memory.Buffer.Buffers;

import com.jopencl.core.memory.Buffer.AbstractBuffer;
import com.jopencl.core.memory.Buffer.Writable;
import com.jopencl.core.memory.Data.Data;

public class ParameterBuffer extends AbstractBuffer implements Writable {
    public ParameterBuffer (Class<Data> clazz) {
        this.setDataClass(clazz)
                .setInitSize(1)
                .init();
    }
}
