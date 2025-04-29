package com.jopencl.core.memory.Data;

public abstract class Data {
    public static Data getInstance() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of " + this.getClass().getName(), e);
        }
    }

}
