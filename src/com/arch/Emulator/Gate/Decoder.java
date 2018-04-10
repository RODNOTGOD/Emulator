package com.arch.Emulator.Gate;

import java.util.Arrays;

public class Decoder implements Gate {

    private final int NORMAL = 0;
    private final int INVERTED = 1;

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

        And andGate = new And(inputs.length, 1);
        int[][] sets = new int[inputs.length][2];
        int marker = sets.length - 1;

        for (int i = 0; i < sets.length; i++) {
            sets[i][0] = inputs[i];
            sets[i][1] = NORMAL;
        }

        for (int i = 0; i < outputs.length; i++) {
            int[] createdInput = new int[inputs.length];
            for (int j = 0; j < sets.length; j++)
                createdInput[j] = sets[j][0];

            andGate.loadArguments(createdInput);
            outputs[i] = andGate.calculate()[0];

            // Find next flippable area
            for (int j = sets.length - 1; j >= 0; j--) {
                if (sets[j][1] == NORMAL) {
                    marker = j;
                    break;
                }
            }

            sets[marker][0] = ~sets[marker][0];
            sets[marker][1] = INVERTED;

            // Flip all previous bits
            for (int j = marker + 1; j < sets.length; j++) {
                sets[j][0] = ~sets[j][0];
                sets[j][1] = NORMAL;
            }
        }

        return outputs;
    }
}
