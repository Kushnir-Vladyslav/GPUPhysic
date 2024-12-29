package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class SingleIntVale extends BufferContext <int[], IntBuffer> {
    SingleIntVale (int value) {
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

    @Override
    public void update() {
    }

    @Override
    public void resize(int newSize) {
        throw new IllegalStateException("Data from arg cannot be read.");
    }

    @Override
    public int[] getData() {
        throw new IllegalStateException("Data from arg cannot be read.");
    }

    @Override
    public void readBuffer() {
        throw new IllegalStateException("Data from arg cannot be read.");
    }

    @Override
    public int getLength() {
        throw new IllegalStateException("Data from arg haven`t length.");
    }
}
