package com.jopencl.core.memory.Util;

import com.jopencl.core.memory.Buffer.AbstractBuffer;
import org.example.OpenCL.OpenClContext;
import org.lwjgl.opencl.CL10;

public class CopyDataBufferToBuffer {
    public static void copyData (OpenClContext openClContext, AbstractBuffer src, AbstractBuffer dst, long size) {
        copyData(openClContext, src, dst, 0, 0, size);
    }

    public static void copyData (OpenClContext openClContext, AbstractBuffer src, AbstractBuffer dst, long srcOffset, long dstOffset, long size) {
//        if (src.getType().equals(dst.getType())) {
//            System.err.println("Copying is not safe.");
//            System.err.println("The data type of the " + src + " buffer is different from the " + dst + " buffer.");
//        }

        CL10.clEnqueueCopyBuffer(
                openClContext.commandQueue,
                src.getClBuffer(),
                dst.getClBuffer(),
                srcOffset,
                dstOffset,
                size,
                null,
                null
        );
    }

    public static void copyData (OpenClContext openClContext, long src, long dst, long size) {
        copyData(openClContext, src, dst, 0, 0, size);
    }

    public static void copyData (OpenClContext openClContext, long src, long dst, long srcOffset, long dstOffset, long size) {

        CL10.clEnqueueCopyBuffer(
                openClContext.commandQueue,
                src,
                dst,
                srcOffset,
                dstOffset,
                size,
                null,
                null
        );
    }
}
