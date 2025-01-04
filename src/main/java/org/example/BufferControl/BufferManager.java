package org.example.BufferControl;

import java.util.HashMap;
import java.util.Map;

public class BufferManager {
    Map<String, BufferContext<?>> buffers;

    public BufferManager () {
        buffers = new HashMap<>();
    }

    public void createBuffer () {

    }

    public <T extends BufferContext<? extends TypeOfBuffer>> T getBuffer (String name) {

        return null;
    }

    public void destroy () {
        for (BufferContext<?> buffer : buffers.values()) {
            buffer.destroy();
        }
        buffers.clear();
    }
}

