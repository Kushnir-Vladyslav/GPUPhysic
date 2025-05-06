package com.jopencl.core.memory.buffer.buffers;

import com.jopencl.core.memory.buffer.AbstractBuffer;
import com.jopencl.core.memory.buffer.AdditionalInitiation;
import com.jopencl.core.memory.data.Data;
import org.example.OpenCL.OpenClContext;

public class LocalBuffer extends AbstractBuffer implements AdditionalInitiation<LocalBuffer> {

    @Override
    public void addInit() {
        if (copyHostBuffer || copyNativeBuffer) {
            System.err.println("LocalBuffer cannot transfer data to the host, so there is no point in creating projections.");
        }
    }

    public void setup (Class<Data> clazz, OpenClContext context, int initSize) {
        setDataClass(clazz);
        setInitSize(initSize);
        setOpenClContext(context);
        init();
    }

    public void setup (String bufferName, Class<Data> clazz, OpenClContext context, int initSize) {
        setBufferName(bufferName);
        setDataClass(clazz);
        setInitSize(initSize);
        setOpenClContext(context);
        init();
    }
}
