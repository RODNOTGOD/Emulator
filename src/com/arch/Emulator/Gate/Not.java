package com.arch.Emulator.Gate;

public class Not implements Gate {

    private int[] inputs;
    private int[] output;
    private int result;

    public Not(int inputs, int outputs) {
        this.inputs = new int[inputs];
        this.output = new int[outputs];
        result = 0;
    }

    @Override
    public int transmit() {
        return result;
    }

    @Override
    public void loadArguments(int[] inputs) {
        if (inputs.length != this.inputs.length)
            throw new IllegalArgumentException("Not matching arguments");
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
    }

    @Override
    // Since ~ bitwise not only works on 32 bits, the mask specifies the 
    // last four bits for the result. Otherwise, it would return a negative
    // number. Still need to work on this.
    public int calculate() {
    	int mask = 0x0000000f;
        result = inputs[0];
        for (int bit : inputs)
            result = (~bit) & mask;
        return result;
    }
}
