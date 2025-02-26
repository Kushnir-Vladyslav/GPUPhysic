package org.example.BufferControl;

import org.example.OpenCL.OpenCL;
import org.example.OpenCL.OpenClContext;
import org.lwjgl.opencl.CL10;

public class CopyDataBufferToBuffer {

    public  CopyDataBufferToBuffer () {
//        CL10.clEnqueueCopyBuffer()
    }

    public void copyData (BufferContext<?> src, BufferContext<?> dst, int size) {
        copyData(src, dst, 0, 0, size);
    }

    public void copyData (BufferContext<?> src, BufferContext<?> dst, int srcOffset, int dstOffset, int size) {
        if (src.getType().equals(dst.getType())) {
            System.err.println("Copying is not safe.");
            System.err.println("The data type of the " + src + " buffer is different from the " + dst + " buffer.");
        }

        CL10.clEnqueueCopyBuffer(
                OpenClContext.getInstance().commandQueue,
                src.getClBuffer(),
                dst.getClBuffer(),
                srcOffset,
                dstOffset,
                size,
                null,
                null
        );
    }
}
