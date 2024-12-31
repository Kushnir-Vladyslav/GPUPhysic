package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class SingleFloatValue extends SingleValue <float[], FloatBuffer> {
    public SingleFloatValue (float value) {
        nativeBuffer = MemoryUtil.memAllocFloat(1).put(0, value);
    }

    @Override
    public void update(float[] newDats) {
        if (hostBuffer.length != 1) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array.");
        }
        nativeBuffer.put(0, newDats[0]).rewind();
        setNewArgs();
    }
}
