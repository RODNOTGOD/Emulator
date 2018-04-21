package com.arch.Emulator.Gates;


public class Or extends Gate {

    // ORs two numbers together

    public Or() {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        //outputs[0] = 0x00000000;
        for (int bit : inputs)
            outputs[0] |= bit;
        return outputs;
    }
}
