package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class SingleIntVale extends SingleValue <int[], IntBuffer> {
    public SingleIntVale (int value) {
        nativeBuffer = MemoryUtil.memAllocInt(1).put(0, value);
    }

    @Override
    public void update(int[] newDats) {
        if (hostBuffer.length != 1) {
            throw new IllegalArgumentException("The array must contain a single number equal to the capacity of the local array.");
        }
        nativeBuffer.put(0, newDats[0]).rewind();
        setNewArgs();
    }

}
