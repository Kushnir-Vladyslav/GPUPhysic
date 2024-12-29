package org.example.BufferControl;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SingleFloatValue extends BufferContext <float[], FloatBuffer> {
    SingleFloatValue (float value) {
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

    @Override
    public void update() {
    }

    @Override
    public void resize(int newSize) {
        throw new IllegalStateException("Data from arg cannot be read.");
    }

    @Override
    public float[] getData() {
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

