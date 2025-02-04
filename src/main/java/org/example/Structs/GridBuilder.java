package org.example.Structs;

import org.example.BufferControl.GlobalDynamicBuffer;
import org.example.BufferControl.TypeOfBuffer.IntBufferType;

public class GridBuilder {

    public float maxParticleSize = 1;

    GlobalDynamicBuffer<IntBufferType> gridIndexes;         // Зберігає масив індексів частинок в клітинах
    GlobalDynamicBuffer<IntBufferType> gridDistribution;    // Зберігає масив з кількістю частинок в клітинах
    GlobalDynamicBuffer<IntBufferType> prefixAmount;        // Префіксна сума щоб знати який відступ робити



}
