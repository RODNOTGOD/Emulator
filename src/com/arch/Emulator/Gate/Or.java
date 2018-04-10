package com.arch.Emulator.Gate;

public class Or extends Gate {

    public Or(int numOfInputs) {
        super(numOfInputs, 1);
    }

    @Override
    public int[] calculate() {
        return outputs;
    }
}
