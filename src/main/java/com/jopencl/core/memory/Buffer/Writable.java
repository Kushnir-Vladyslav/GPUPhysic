package com.jopencl.core.memory.Buffer;

import org.lwjgl.opencl.CL10;

public interface Writable <T extends AbstractBuffer & Writable<T>> {
    default void write() {
        writeTo(0);
    }

     default void writeTo(long offset) {
         T buffer = (T) this;

         CL10.clEnqueueWriteBuffer(
                 buffer.openClContext.commandQueue,
                 buffer.clBuffer,
                 true,
                 offset,
                 buffer.nativeBuffer,
                 null,
                 null
         );
    }
}
