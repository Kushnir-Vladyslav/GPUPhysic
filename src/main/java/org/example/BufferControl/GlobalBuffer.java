package org.example.BufferControl;

import java.nio.Buffer;

public abstract class GlobalBuffer<T, K extends Buffer> extends BufferContext<T, K> {
    protected int capacity;
    protected boolean isDynamic;

    protected abstract void reWrightBuffers (boolean isNewBuffers);
    protected abstract void addToEnd(T data);

    @Override
    public void update() {
        reWrightBuffers(false);
    }
}
