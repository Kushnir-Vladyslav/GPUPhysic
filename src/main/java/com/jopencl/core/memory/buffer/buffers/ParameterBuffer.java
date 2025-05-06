package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.buffer.AdditionalInitiation;
import com.jopencl.core.memory.data.ConvertToByteBuffer;
import com.jopencl.core.memory.data.Data;

public class ParameterBuffer extends AbstractBuffer implements AdditionalInitiation<ParameterBuffer> {
    public ParameterBuffer (Class<Data> clazz) {
        this.setDataClass(clazz)
                .setInitSize(1)
                .setCopyNativeBuffer(true)
                .init();
    }

    @Override
    public void addInit() {
        if (!(dataObject instanceof ConvertToByteBuffer)) {
            initErr("Data class doesn't extends of \"ConvertToByteBuffer\" interface.");
        }
    }

    public void setParameter (Object object) {
        ((ConvertToByteBuffer) dataObject).convertToByteBuffer(nativeBuffer,object);
    }


}
