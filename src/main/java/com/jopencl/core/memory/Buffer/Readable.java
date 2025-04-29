package com.jopencl.core.memory.Buffer;

import org.example.OpenCL.OpenClContext;
import org.lwjgl.opencl.CL10;

import java.nio.ByteBuffer;

public interface Readable {
    default void read(OpenClContext openClContext, long clBuffer, ByteBuffer nativeBuffer) {
        readFrom(openClContext, clBuffer, 0, nativeBuffer);
    }

     default void readFrom(OpenClContext openClContext, long clBuffer, long offset, ByteBuffer nativeBuffer) {
        CL10.clEnqueueReadBuffer(
                openClContext.commandQueue,
                clBuffer,
                true,
                offset,
                nativeBuffer,
                null,
                null
        );
    }
}
