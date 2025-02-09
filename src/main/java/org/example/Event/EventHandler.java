package org.example.Event;

@FunctionalInterface
public interface EventHandler<T> {
    public void handle (T eventData);
}

