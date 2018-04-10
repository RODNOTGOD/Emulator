package com.arch.Emulator.Gate;

public class And implements Gate {

    private  int[] inputs;
    private int[] output;

    public And(int numOfInputs, int numOfOutputs) {
        this.inputs = new int[numOfInputs];
        this.output = new int[numOfOutputs];
    }

    @Override
    public int[] transmit() {
        return output;
    }

    @Override
    public void loadArguments(int[] inputs) {
        if (inputs.length != this.inputs.length)
            throw new IllegalArgumentException("Not matching arguments");
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
    }

    @Override
    public int[] calculate() {
        output[0] = 0xFFFFFFFF;
        for (int bit : inputs)
            output[0] &= bit;
        return output;
    }
}
