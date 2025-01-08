package org.example.BufferControl;

import java.util.HashMap;
import java.util.Map;

public class BufferManager {
    Map<String, BufferContext<?>> buffers;

    public BufferManager () {
        buffers = new HashMap<>();
    }

    public <K extends TypeOfBuffer, T extends BufferContext<K>> T createBuffer (String name, Class<T> contextType, Class<K> bufferType) {
        if (buffers.containsKey(name)) {
            return getBuffer(name, contextType, bufferType);
        }

        try {
            T newBuffer = contextType.getConstructor(Class.class).newInstance(bufferType);

            buffers.put(name, newBuffer);

            return newBuffer;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to create new buffer", e);
        }
    }

    public <K extends TypeOfBuffer, T extends BufferContext<K>> T getBuffer(String name, Class<T> contextType, Class<K> bufferType) {
        BufferContext<?> bufferContext = buffers.get(name);
        if (bufferContext == null) {
            throw new IllegalArgumentException("Buffer not found: " + name);
        }

        // Перевірка типу контексту
        if (!contextType.isInstance(bufferContext)) {
            throw new IllegalArgumentException("Buffer type mismatch: expected "
                    + contextType.getSimpleName() + ", found " + bufferContext.getClass().getSimpleName());
        }

        // Перевірка параметризованого типу
        T casted = contextType.cast(bufferContext);
        if (!casted.getType().equals(bufferType)) {
            throw new IllegalArgumentException("Buffer parameter type mismatch: expected "
                    + bufferType.getSimpleName() + ", found " + casted.getType().getSimpleName());
        }

        return casted;
    }

    public boolean isExist (String name) {
        return buffers.containsKey(name);
    }

    public void destroy () {
        for (BufferContext<?> buffer : buffers.values()) {
            buffer.destroy();
        }
        buffers.clear();
    }
}

