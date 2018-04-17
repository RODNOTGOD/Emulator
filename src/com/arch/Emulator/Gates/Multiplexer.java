package com.arch.Emulator.Gates;


/**
 *  Takes multiple input lines and, using switches, selects one line to receive
 *  data from. The number of selectors is log2(n), where n is the number of input lines.
 *
 */

public class Multiplexer extends Gate {

    public Multiplexer(int numOfInputs) {
        super(numOfInputs, 1);

    }

    @Override
    public int[] calculate() {
        StringBuilder builder = new StringBuilder();
        String binary;

        assert inputs != null;
        assert outputs != null;
        assert inputSelectors != null;


        // The number of input lines must be a power of two.
        if (!IsPowerOfTwo(inputs.length)){
            throw new IllegalArgumentException(Integer.toString(inputs.length));
        }

        // This takes in the selector inputs and converts it from binary to decimal,
        // which will equal the number of the selected line.
        for (int i: inputSelectors) {
            builder.append(i);
        }

        binary = builder.toString();
        outputs[0] = inputs[Integer.parseInt(binary, 2)];

        return outputs;
    }

    public static boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }
}

