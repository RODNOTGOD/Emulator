package com.arch.Emulator.Gates;

public class Xor extends Gate {

    public Xor(int numOfInputs) {
        super(numOfInputs, 1);
    }

    @Override
    public int[] calculate() {
        outputs[0] = inputs[0];
        for(int i = 1; i < inputs.length; i++){
            outputs[0] ^= inputs[i];
        }
        return outputs;
    }
}