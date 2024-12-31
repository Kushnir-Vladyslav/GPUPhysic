package org.example.BufferControl;

import java.nio.Buffer;

public abstract class ReadBuffer<T, K extends Buffer> extends BufferContext<T, K> {

    protected abstract void newBuffers();

    @Override
    public void update() {
    }

    @Override
    public void resize (int newSize) {
        if (length == newSize) {
            return;
        }
        length = newSize;

        newBuffers();
    }

    @Override
    public void update(T newData) {
        throw new IllegalStateException("The buffer is read-only and cannot write data.");
    }
}
