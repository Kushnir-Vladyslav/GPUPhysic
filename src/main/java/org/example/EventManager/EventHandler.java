package org.example.EventManager;

@FunctionalInterface
public interface EventHandler<T> {
    public void handle (T eventData);
}

