package com.arch.Emulator.Gates;

public class Decoder extends Gate {

    private final int NORMAL = 0;
    private final int INVERTED = 1;

    public Decoder(int numOfInputs) {
        super(numOfInputs, (int) Math.pow(2, numOfInputs));
    }

    @Override
    public int[] calculate() {

        assert inputs != null;
        assert outputs != null;

        And andGate = new And();
        int[][] sets = new int[inputs.length][2];
        int marker = sets.length - 1;

        for (int i = 0; i < sets.length; i++) {
            sets[i][0] = inputs[i];
            sets[i][1] = NORMAL;
        }

        for (int i = 0; i < outputs.length; i++) {
            int[] createdInput = new int[inputs.length];
            for (int j = 0; j < sets.length; j++)
                createdInput[j] = 0x1 & sets[j][0];

            andGate.loadArguments(createdInput);
            outputs[i] = andGate.calculate()[0];

            // Find next flippable area
            for (int j = sets.length - 1; j >= 0; j--) {
                if (sets[j][1] == NORMAL) {
                    marker = j;
                    break;
                }
            }

            sets[marker][0] = 0x1 & ~sets[marker][0];
            sets[marker][1] = INVERTED;

            // Flip all previous bits
            for (int j = marker + 1; j < sets.length; j++) {
                sets[j][0] = 0x1 & ~sets[j][0];
                sets[j][1] = NORMAL;
            }
        }

        return outputs;
    }
}
