package org.example.Kernel.PrefixSum;

import org.example.BufferControl.BufferContext;
import org.example.Kernel.Kernel;


public class CalculationPrefixAmount extends Kernel {

    final int LOCAL_WORK_SIZE = 256;

    private BufferContext<?> soursBuffer;
    private BufferContext<?> destinationBuffer;



    public CalculationPrefixAmount () {
        super("CalculationPrefixAmount", "CalculationPrefixAmount.cl");
    }

    @Override
    public void run() {

    }
}
