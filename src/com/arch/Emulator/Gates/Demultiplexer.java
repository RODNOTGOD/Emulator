package com.arch.Emulator.Gates;


/**
 *  Takes data from one or more input lines and, using selectors,
 *  selects one line to receive data from.
 *
 */

public class Demultiplexer extends Gate {

    public Demultiplexer(int numOfInputs) {
        super(numOfInputs, (int) Math.pow(2, numOfInputs));

    }

    @Override
    @SuppressWarnings("Duplicates")
    public int[] calculate() {
        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        assert inputs != null;
        assert outputs != null;
        assert inputSelectors != null;
        assert outputSelectors != null;


        // The number of output lines must be a power of two.
        if (!IsPowerOfTwo(outputs.length)){
            throw new IllegalArgumentException(Integer.toString(outputs.length));
        }

        //Clear outputs before calculation
        for (int i=0; i < outputs.length; i++ ){
            outputs[i] = 0;
        }

        // This takes in the selector inputs and outputs and converts it
        // from binary to decimal, which will equal the number of the selected line.
        for (int j: inputSelectors) {
            builder.append(j);
        }
        for (int k: outputSelectors) {
            builder2.append(k);
        }

        String inputBinary = builder.toString();
        String outputBinary = builder2.toString();

        // Selects the data from the selected line using inputSelectors and places it at the selected
        // position in the array using outputSelectors.
        outputs[Integer.parseInt(outputBinary, 2)] = inputs[Integer.parseInt(inputBinary, 2)];
        return outputs;
    }

    public static boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }
}

