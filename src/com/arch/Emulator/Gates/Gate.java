package com.arch.Emulator.Gates;

public abstract class Gate {

    int[] inputs;
    int[] outputs;
    int[] inputSelectors;
    int[] outputSelectors;

    public Gate(int numOfInputs, int numOfOutputs) {
        inputs = new int[numOfInputs];
        outputs = new int[numOfOutputs];
    }

    /**
     * Gets the output of the gate
     *
     * @return the calculated output of the gate
     */
    public int[] transmit() {
        return outputs;
    }

    /**
     * Loads all argument into the gate as inputs
     * and sets them to calculated by the gate
     *
     * @param inputs wire inputs to the gate
     */
    public void loadArguments(int[] inputs) {
        assert inputs != null;
        if (inputs.length != this.inputs.length)
            throw new IllegalArgumentException("Not matching arguments");
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
    }

    /**
     * Sets the required selectors lines for mux/demux gates
     *
     * @param controlLines a binary array to select a line
     */
    public void setInputSelector(int[] controlLines){
        inputSelectors = controlLines;
    }

    public abstract int[] calculate();
}
