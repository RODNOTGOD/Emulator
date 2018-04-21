package com.arch.Emulator.Gates;

public class Xor extends Gate {

    public Xor() {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        outputs[0] =0x00000000;
        for(int bit: inputs)
            outputs[0] ^= bit;
        return outputs;
    }
}