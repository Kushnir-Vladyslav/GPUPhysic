package com.jopencl.core.memory.Data;

import java.nio.ByteBuffer;

public interface ConvertFromByteBuffer {
    void convertFromByteBuffer (ByteBuffer nativeBuffer, Object[] obj);

    Object[] createArr (int len);
}
