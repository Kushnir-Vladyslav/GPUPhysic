package org.example.BufferControl;

import org.example.BufferControl.TypeOfBuffer.TypeOfBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 * Клас для управління буферами.
 * Дозволяє створювати, отримувати та видаляти буфери.
 */
public class BufferManager {
    protected Map<String, BufferContext<?>> buffers; // Мапа для зберігання всіх створених буферів

    public BufferManager () {
        buffers = new HashMap<>();
    }

    /**
     * Створює новий буфер.
     *
     * @param name        Назва буфера.
     * @param contextType Тип буфера.
     * @param bufferType  Тип даних що будуть передаватись.
     * @param <K>         Тип даних що будуть передаватись.
     * @param <T>         Тип буфера.
     * @return Створений контекст буфера.
     * @throws IllegalArgumentException Якщо не вдається створити буфер.
     */
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

    /**
     * Отримує буфер за назвою.
     *
     * @param name        Назва буфера.
     * @param contextType Тип буфера.
     * @param bufferType  Тип даних що будуть передаватись.
     * @param <K>         Тип даних що будуть передаватись.
     * @param <T>         Тип буфера.
     * @return Контекст буфера.
     * @throws IllegalArgumentException Якщо буфер не існує або типи не співпадають.
     */
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

    /**
     * Видаляє буфер за назвою.
     *
     * @param name Назва буфера.
     * @throws IllegalArgumentException Якщо буфер не існує.
     */
    public void removeBuffer(String name) {
        if(!isExist(name)) {
            throw new IllegalArgumentException("A buffer with name \"" + name + "\" does not exist.");
        } else {
            buffers.remove(name);
        }
    }

    /**
     * Перевіряє, чи існує буфер з заданою назвою.
     *
     * @param name Назва буфера.
     * @return true, якщо буфер існує, інакше false.
     */
    public boolean isExist (String name) {
        return buffers.containsKey(name);
    }

    /**
     * Звільняє всі ресурси буферів.
     */
    public void destroy () {
        for (BufferContext<?> buffer : buffers.values()) {
            buffer.destroy();
        }
        buffers.clear();
    }
}

