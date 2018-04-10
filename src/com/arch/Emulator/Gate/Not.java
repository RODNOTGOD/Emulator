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
    // mask should fix the ~ bitwise not operator only converting
    // 32 bit. Will test some more.
    public int[] calculate() {
        output[0] = inputs[0];
        int mask = 0x0000000f;
        for (int bit : inputs)
            output[0] = (~bit) & mask;
        return output;
    }
}
