package com.arch.Emulator.Gate;

public abstract class Gate {

    int[] inputs;
    int[] outputs;
    int[] selectors;

    public Gate(int numOfInputs, int numOfOutputs) {
        inputs = new int[numOfInputs];
        outputs = new int[numOfOutputs];
    }

    public int[] transmit() {
        return outputs;
    }

    public void loadArguments(int[] inputs) {
        assert inputs != null;
        if (inputs.length != this.inputs.length)
            throw new IllegalArgumentException("Not matching arguments");
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
    }

    public void setSelector(int[] controlLines){
        selectors = controlLines;
    }

    public abstract int[] calculate();
}
