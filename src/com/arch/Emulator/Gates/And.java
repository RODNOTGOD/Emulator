package com.arch.Emulator.Gates;

public class And extends Gate {

    public And() {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        outputs[0] = 0xFFFFFFFF;
        for (int bit : inputs)
            outputs[0] &= bit;
        return outputs;
    }
}
