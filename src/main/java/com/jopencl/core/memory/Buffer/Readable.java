package com.jopencl.core.memory.Buffer;

import com.jopencl.core.memory.Data.ConvertFromByteBuffer;
import org.lwjgl.opencl.CL10;

public interface Readable <T extends AbstractBuffer & Readable<T>> {
    default Object read() {
        return readFrom(0);
    }

     default Object readFrom(long offset) {
         T buffer = (T) this;

         ConvertFromByteBuffer converter = (ConvertFromByteBuffer) buffer.dataObject;

         if (buffer.projectionToHost) {
             CL10.clEnqueueReadBuffer(
                     buffer.openClContext.commandQueue,
                     buffer.clBuffer,
                     true,
                     offset,
                     buffer.nativeBuffer,
                     null,
                     null
             );

             converter.convertFromByteBuffer(buffer.nativeBuffer, buffer.hostBuffer);

             return buffer.hostBuffer;
         }


    }
}
