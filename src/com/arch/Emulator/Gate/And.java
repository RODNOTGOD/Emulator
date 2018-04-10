package com.arch.Emulator.Gate;

public class And extends Gate {

    public And(int numOfInputs) {
        super(numOfInputs, 1);
    }

    @Override
    public int[] calculate() {
        outputs[0] = 0xFFFFFFFF;
        for (int bit : inputs)
            outputs[0] &= bit;
        return outputs;
    }
}
