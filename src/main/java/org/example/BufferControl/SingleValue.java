package org.example.BufferControl;

import java.nio.Buffer;

public abstract class SingleValue <T, K extends Buffer> extends BufferContext<T, K> {
    @Override
    public void update() {
    }

    @Override
    public void resize(int newSize) {
        throw new IllegalStateException("Data from arg cannot be read.");
    }

    @Override
    public T getData() {
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
