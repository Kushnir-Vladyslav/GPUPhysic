package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.data.Data;

public class LocalBuffer extends AbstractBuffer {

    public void setup (Class<Data> clazz, int len) {
        this.setDataClass(clazz)
                .setInitSize(len)
                .init();
    }


}
