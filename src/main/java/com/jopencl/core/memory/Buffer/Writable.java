package com.jopencl.core.memory.Buffer;

import org.example.OpenCL.OpenClContext;
import org.lwjgl.opencl.CL10;

import java.nio.ByteBuffer;

public interface Writable {
    default void write(OpenClContext openClContext, long clBuffer, ByteBuffer nativeBuffer) {
        writeTo(openClContext, clBuffer, 0, nativeBuffer);
    }

     default void writeTo(OpenClContext openClContext, long clBuffer, long offset, ByteBuffer nativeBuffer) {
        CL10.clEnqueueWriteBuffer(
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
