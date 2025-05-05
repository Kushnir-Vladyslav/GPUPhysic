package com.jopencl.core.memory.Buffer;

import org.lwjgl.opencl.CL10;

public interface Readable {
    default void read() {
        readFrom(0);
    }

     default void readFrom(long offset) {
         if (this instanceof AbstractBuffer abstractBuffer) {
             CL10.clEnqueueReadBuffer(
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
