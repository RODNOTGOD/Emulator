package com.arch.Emulator.Gate;

import java.util.Arrays;

public class Decoder implements Gate {

    private int[] inputs = null;
    private int[] outputs = null;

    public Decoder(int numOfInputs, int numOfOutputs) {
        int realInputs = (int) Math.floor(Math.log(numOfOutputs) / Math.log(2));

        if (realInputs != numOfInputs) {
            throw new IllegalArgumentException("Wrong amount of inputs/outputs: need "
                    + numOfInputs + " inputs with "
                    + (int) Math.pow(2, numOfInputs) + " outputs or "
                    + realInputs + " inputs with " + numOfOutputs + " outputs");
        }

        inputs = new int[numOfInputs];
        outputs = new int[numOfOutputs];
    }

    @Override
    public int[] transmit() {
        return outputs;
    }

    @Override
    public void loadArguments(int[] inputs) {
        assert inputs != null;
        if (inputs.length != this.inputs.length)
            throw new IllegalArgumentException("Not matching arguments");
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
    }

    @Override
    public int[] calculate() {

        assert inputs != null;
        assert outputs != null;

        int[] sets = inputs.clone();
        int marker = sets.length - 1;
        And andGate = new And(inputs.length, 1);

        for (int i = 0; i < outputs.length; i++) {
            andGate.loadArguments(sets);
            outputs[i] = andGate.calculate()[0];
            sets[marker] = ~sets[marker];
            for (int j = marker + 1; j < sets.length; j++)
                sets[j] = ~sets[j];
            marker--;
        }

        return outputs;
    }
}
