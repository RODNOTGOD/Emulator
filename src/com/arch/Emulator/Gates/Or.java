package com.arch.Emulator.Gates;

public class Or extends Gate {

    // Will add two numbers if the selector is set to 0, and subtract if it
    // is set to 1. If the sum is greater than

    public Or() {
        super(2, 1);
    }

    @Override
    public int[] calculate (){
        return outputs;
    }
}
