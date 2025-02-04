package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;

import java.util.HashMap;
import java.util.Map;

public class BufferManager {
    protected Map<String, BufferContext<?>> buffers;

    public BufferManager () {
        buffers = new HashMap<>();
    }

    public <K extends TypeOfBuffer, T extends BufferContext<K>> T createBuffer (String name, Class<T> contextType, Class<K> bufferType) {
        if (isExist(name)) {
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
        if(!isExist(name)) {
            throw new IllegalArgumentException("A buffer with that name does not exist.");
        }
        BufferContext<?> bufferContext = buffers.get(name);

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

    public void removeBuffer(String name) {
        if(!isExist(name)) {
            throw new IllegalArgumentException("A buffer with name \"" + name + "\" does not exist.");
        } else {
            buffers.remove(name);
        }
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

