package org.example.BufferControl;

import java.util.HashMap;
import java.util.Map;

public class BufferManager {
    Map<String, BufferContext<?, ?>> bufferContextMap;

    public BufferManager () {
        bufferContextMap = new HashMap<>();
    }

    public void set (String name, BufferContext<?, ?> bufferContext) {
        if (bufferContextMap.containsKey(name)) {
            if (!bufferContextMap.get(name).getClass().equals(bufferContext.getClass())) {
                throw new IllegalArgumentException("The classes of the existing and new values do not match.");
            }
        } else {
            bufferContextMap.put(name, bufferContext);
        }
    }

    public BufferContext<?, ?> get (String name) {
        if (!bufferContextMap.containsKey(name)) {
            throw new IllegalArgumentException("An element with such a key does not exist.");
        } else {
            return bufferContextMap.get(name);
        }
    }

    public void destroy () {
        for (BufferContext<?, ?> buffer : bufferContextMap.values()) {
            buffer.destroy();
        }
        bufferContextMap.clear();
    }

    public boolean isExist (String name) {
        return bufferContextMap.containsKey(name);
    }
}
