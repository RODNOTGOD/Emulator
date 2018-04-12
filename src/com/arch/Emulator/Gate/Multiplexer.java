package com.arch.Emulator.Gate;

import java.util.Arrays;

/**
 *  Takes multiple inputs and, using selector switches, returns one output.
 *  The number of selectors is log2(n), where n is the number of inputs.
 */

public class Multiplexer extends Gate {

    public Multiplexer(int numOfInputs) {
        super(numOfInputs, 1);

    }

    @Override
    public int[] calculate() {
        int numOfSelectors;

        assert inputs != null;
        assert outputs != null;

        // The number of inputs must be a power of two.
        if (!IsPowerOfTwo(inputs.length)){
            throw new IllegalArgumentException(Integer.toString(inputs.length));
        }

        numOfSelectors = (int) (Math.log(inputs.length)/Math.log(2));

        // Logic here

        return new int[0];
    }

    public static boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }
}

