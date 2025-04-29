package com.jopencl.core.memory.Buffer.Buffers;

import com.jopencl.core.memory.Buffer.AbstractBuffer;
import com.jopencl.core.memory.Data.Data;

public class ParameterBuffer extends AbstractBuffer {

    public ParameterBuffer (Class<Data> clazz) {
        this.setDataClass(clazz);

    }
}
