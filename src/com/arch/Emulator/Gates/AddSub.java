package com.arch.Emulator.Gates;

/**
 * Depending on the carry in bit, adds or subtracts two 16 bit numbers and returns
 * the sum/difference as an int. Also sets the carry, overflow, negative, and zero flags.
 *
 * Usage:
 */

public class AddSub extends Gate{
    final int MAX = 0xff;

    public AddSub() {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        int zeroFlag = 0;
        int carryFlag = 0;
        int overflowFlag = 0;
        int negativeFlag = 0;
        int result;

        assert inputs != null;
        assert outputs != null;
        assert inputSelectors != null;      // input selector acts as carry in bit.
        assert outputSelectors != null;     // output selectors act as flags.

        if (inputSelectors[0] == 0){        // if carry in bit set to 0, add.
            result = add(inputs[0], inputs[1]);
        }else{
            result = sub(inputs[0], inputs[1]);
        }
        if (result < 0)
            negativeFlag = 1;
        if (result > MAX)
            overflowFlag = 1;


        outputs[0] = result;
        return outputs;
    }

    //TODO: Handle flags
    public int add(int x, int y){
        int carry;
        while(y != 0) {
            carry = x & y;
            x = x ^ y;
            y = carry << 1;
        }
        return x;
    }

    private int sub(int x, int y){

        int diff = x - y;
        return diff;
    }

}
