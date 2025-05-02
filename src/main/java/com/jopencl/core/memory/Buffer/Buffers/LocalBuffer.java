package com.jopencl.core.memory.Buffer.Buffers;

import com.jopencl.core.memory.Buffer.AbstractBuffer;
import com.jopencl.core.memory.Data.Data;

public class LocalBuffer extends AbstractBuffer {

    public void setup (Class<Data> clazz, int len) {
        this.setDataClass(clazz)
                .setInitSize(len)
                .init();
    }

}
