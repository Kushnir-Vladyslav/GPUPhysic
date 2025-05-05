package com.jopencl.core.memory.Buffer;

import org.example.OpenCL.OpenClContext;
import org.lwjgl.opencl.CL10;

import java.nio.ByteBuffer;

public interface Writable {
    default void write() {
        writeTo(0);
    }

     default void writeTo(long offset) {
         if (this instanceof AbstractBuffer abstractBuffer) {
             CL10.clEnqueueWriteBuffer(
                     abstractBuffer.getOpenClContext().commandQueue,
                     abstractBuffer.getClBuffer(),
                     true,
                     offset,
                     abstractBuffer.getNativeBuffer(),
                     null,
                     null
             );
         }
    }
}
