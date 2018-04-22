package com.arch.Emulator.Gates;

import org.omg.PortableInterceptor.INACTIVE;

/**
 * Depending on the carry in bit, adds or subtracts two 16 bit numbers and returns
 * the sum/difference as an int. Also sets the carry, overflow, negative, and zero flags.
 *
 * Usage:
 */

public class AddSub extends Gate {

    // ZERO, OVERFLOW, SIGNED, CARRY
    public static int pubFlags = 0x0;
    private static int flags = 0x0;

    boolean addit = true;
    boolean enabled = false;

    public AddSub() {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        int result = 0;

        assert inputs != null;
        assert outputs != null;
        assert inputSelectors != null;      // input selector acts as carry in bit.
        assert outputSelectors != null;     // output selectors act as flags.

        if (addit)
            result = add(inputs[0] & 0xFF, inputs[1] & 0xFF);
        else
            result = add((inputs[0]) & 0xFF, ~inputs[1] + 1 & 0xFF);
        flags = pubFlags;

        outputs[0] = result & 0xFF;
        System.out.println("Result: " + Integer.toHexString(outputs[0]));
        return outputs;
    }

    public int getFlags() {
        return flags;
    }

    static public int add(int x, int y) {
        int carry = 0;
        int result = x;
        int status = x>>>7 ^ y >>> 7;
        while (y != 0) {
            carry = result & y;
            result = result ^ y;
            y = carry << 1;
        }
        if (result >>> 7 != x >>> 7 && status == 0)
            pubFlags |= 0b0100;
        else
            pubFlags &= 0b1011;
        if (result > 0xFF || result < -0xFF)
            pubFlags |= 0b0001;
        else
            pubFlags &= 0b1110;
        if ((result & 0x0FF) == 0)
            pubFlags |= 0b1000;
        else
            pubFlags &= 0b0111;
        if ((result & 0x080) == 0x80)
            pubFlags |= 0b0010;
        else
            pubFlags &= 0b1101;
        return result;
    }

    static public int sub(int x, int y) {
        int borrow;
        while (y!=0) {
            borrow = (~x) & y;
            x = x ^ y;
            y = borrow << 1;
        }

        return x;
    }

    public void setAdder() {
        addit = true;
    }

    public void setSubber() {
        addit = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enableFlags() {
        enabled = true;
    }

    public void disableFlags() {
        enabled = true;
    }
}
