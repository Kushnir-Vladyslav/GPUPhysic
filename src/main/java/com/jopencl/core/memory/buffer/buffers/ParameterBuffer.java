package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.buffer.AdditionalInitiation;
import com.jopencl.core.memory.data.ConvertToByteBuffer;
import com.jopencl.core.memory.data.Data;
import org.example.OpenCL.OpenClContext;

public class ParameterBuffer extends AbstractBuffer implements AdditionalInitiation<ParameterBuffer> {
    public ParameterBuffer () {
        setInitSize(1);
        setCopyNativeBuffer(true);
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

    public void setup (Class<Data> clazz, OpenClContext context) {
        setDataClass(clazz);
        setOpenClContext(context);
        init();
    }

    public void setup (String bufferName, Class<Data> clazz, OpenClContext context) {
        setBufferName(bufferName);
        setDataClass(clazz);
        setOpenClContext(context);
        init();
    }

    public void setParameter (Object object) {
        ((ConvertToByteBuffer) dataObject).convertToByteBuffer(nativeBuffer,object);
    }


}
