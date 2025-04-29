package com.jopencl.core.memory.Data;

import java.nio.ByteBuffer;

public interface ConvertFromByteBuffer {
    Object[] convertFromByteBuffer (ByteBuffer nativeBuffer);
}
