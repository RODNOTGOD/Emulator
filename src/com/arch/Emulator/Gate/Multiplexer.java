package com.arch.Emulator.Gate;

import java.util.Arrays;


/**
 *  Takes multiple input lines and, using switches, selects one line to receive
 *  input from. The number of selectors is log2(n), where n is the number of input lines.
 *
 */

public class Multiplexer extends Gate {

    public Multiplexer(int numOfInputs) {
        super(numOfInputs, 1);

    }

    @Override
    public int[] calculate() {
        StringBuilder builder = new StringBuilder();
        int selectedLine;


        assert inputs != null;
        assert outputs != null;


        // The number of input lines must be a power of two.
        if (!IsPowerOfTwo(inputs.length)){
            throw new IllegalArgumentException(Integer.toString(inputs.length));
        }

        // This takes in the selector inputs and converts it from binary to decimal,
        // which will equal the number of the selected line.

        String selectors = Arrays.toString(inputs);
        for (int i: inputs) {
            builder.append(i);
        }

        String binary = builder.toString();

        outputs[0] = Integer.parseInt(binary, 2);
        System.out.println(outputs[0]);
        System.out.println(outputs);

        //TODO: Set the output to equal the value of data on line[selectedLine]
        // for example, if line 2 is selected, the output will be whatever the value passed
        // from line 2 would be and not just the number 2.
        return outputs;
    }

    public static boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }
}

