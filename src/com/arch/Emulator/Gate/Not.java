package com.arch.Emulator.Gate;

public class Not extends Gate {

    public Not() {
        super(1, 1);
    }

    @Override
    // mask should fix the ~ bitwise not operator only converting
    // 32 bit. Will test some more.
    public int[] calculate() {
        outputs[0] = inputs[0];
        int mask = 0x00000001;
        for (int bit : inputs)
            outputs[0] = (~bit) & mask;
        return outputs;
    }
}
