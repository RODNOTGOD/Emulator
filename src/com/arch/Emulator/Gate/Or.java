package com.arch.Emulator.Gate;

public class Or implements Gate {

    private int[] inputs;
    private int[] output;
    private int result;

    @Override
    public int[] transmit() {
        return output;
    }

    @Override
    public void loadArguments(int[] inputs) {

    }

    @Override
    public int[] calculate() {
        return output;
    }
}
