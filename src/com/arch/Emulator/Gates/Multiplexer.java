package com.arch.Emulator.Gates;


/**
 *  Takes multiple input lines and, using switches, selects one line to receive
 *  data from. The number of selectors is log2(n), where n is the number of input lines.
 *
 */

public class Multiplexer extends Gate {

    final private int READ_WRITE = 0b11;
    final private int READ = 0b10;
    final private int WRITE = 0b01;
    final private int OFF = 0b00;

    private int lineState;

    public Multiplexer(int numOfInputs) {
        super(numOfInputs, 1);
        lineState = 11;
    }

    @Override
    public int[] calculate() {
        if (lineState != 0) {
            StringBuilder builder = new StringBuilder();
            String binary;

            if (inputs == null) throw new AssertionError("input is null");
            if (outputs == null) throw new AssertionError("outputs is null");
            if (inputSelectors == null) throw new AssertionError("selector is null");


            // The number of input lines must be a power of two.
            if (!IsPowerOfTwo(inputs.length)) {
                throw new IllegalArgumentException(Integer.toString(inputs.length));
            }

            // This takes in the selector inputs and converts it from binary to decimal,
            // which will equal the number of the selected line.
            for (int i : inputSelectors) {
                builder.append(i);
            }

            binary = builder.toString();
            outputs[0] = inputs[Integer.parseInt(binary, 2)];
        }
        return outputs;
    }

    private boolean IsPowerOfTwo(int x){
        return (x != 0) && ((x & (x-1)) == 0);
    }

    public void setReadWrite() {
        lineState = 0b11;
    }

    public void setWrite() {
        lineState = 0b01;
    }

    public void setRead() {
        lineState = 0b10;
    }

    public void setOff() {
        lineState = 0;
    }

    public boolean isEnabled() {
        return lineState != 0;
    }

    public boolean canWrite() {
        return (lineState & 0b01) == 0b01;
    }

    public boolean canRead() {
        return (lineState & 0b10) == 0b10;
    }
}

