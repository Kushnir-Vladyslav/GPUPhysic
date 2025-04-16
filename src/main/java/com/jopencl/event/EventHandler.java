package com.jopencl.event;

@FunctionalInterface
public interface EventHandler<T> {
    public void handle (T eventData);
}

