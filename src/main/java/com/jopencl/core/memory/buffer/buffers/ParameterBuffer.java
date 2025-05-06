package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.buffer.AdditionalInitiation;
import com.jopencl.core.memory.data.ConvertToByteBuffer;

public class ParameterBuffer extends AbstractBuffer implements AdditionalInitiation<ParameterBuffer> {
    public ParameterBuffer () {
        super.setInitSize(1);
        this.setCopyNativeBuffer(true);
    }

    @Override
    public void addInit() {
        if (capacity != 1) {
            throw new IllegalStateException("ParameterBuffer can only have a unit size.");
        }

        if (!(dataObject instanceof ConvertToByteBuffer)) {
            initErr("Data class doesn't extends of \"ConvertToByteBuffer\" interface.");
        }
    }

    public void setParameter (Object object) {
        ((ConvertToByteBuffer) dataObject).convertToByteBuffer(nativeBuffer,object);
    }


}
