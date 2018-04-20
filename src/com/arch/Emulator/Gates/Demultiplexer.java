package com.arch.Emulator.Gates;


/**
 *  Takes data from one or more input lines and, using selectors,
 *  selects one line to receive data from.
 *
 */

public class Demultiplexer extends Gate {

    private boolean lineState;

    public Demultiplexer(int numOfInputs) {
        super(numOfInputs, (int) Math.pow(2, numOfInputs));

    }

    @Override
    @SuppressWarnings("Duplicates")
    public int[] calculate() {
        if (lineState) {
            StringBuilder builder = new StringBuilder();

            if (inputs == null) throw new AssertionError();
            if (outputs == null) throw new AssertionError();
            if (inputSelectors == null) throw new AssertionError();

            // The number of output lines must be a power of two.
            if (!IsPowerOfTwo(outputs.length)){
                throw new IllegalArgumentException(Integer.toString(outputs.length));
            }

            // Clear outputs before calculation
            for (int i=0; i < outputs.length; i++ ){
                outputs[i] = 0;
            }
            // This takes in the selector inputs and outputs and converts it
            // from binary to decimal, which will equal the number of the selected line.
            for (int j: inputSelectors) {
                builder.append(j);
            }

            String inputBinary = builder.toString();

            // Selects the data from the selected line using inputSelectors and places it at the selected
            // position in the array using outputSelectors.
            outputs[Integer.parseInt(inputBinary, 2)] = inputs[0];

        }
        return outputs;
    }

    @Override
    public void setInputSelector(int[] controlLines) {
        this.inputSelectors = controlLines;
        this.outputs = new int[(int) Math.pow(2, controlLines.length)];
    }


    private boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }

    public void enabled() {
        lineState = true;
    }

    public void disabled() {
        lineState = false;
    }

    public boolean isEnabled() {
        return lineState;
    }
}

