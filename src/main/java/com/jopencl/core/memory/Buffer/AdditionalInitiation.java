package com.jopencl.core.memory.Buffer;

public interface AdditionalInitiation <T extends AbstractBuffer & AdditionalInitiation<T>> {
    void addInit();
}
