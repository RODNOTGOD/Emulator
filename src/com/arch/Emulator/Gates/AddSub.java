package com.arch.Emulator.Gates;

public class AddSub extends Gate{

    public AddSub(int input1, int input2) {
        super(2, 1);
    }

    @Override
    public int[] calculate() {
        if (inputSelectors[0] == 0){
            outputs[0] = add(inputs[1], inputs[0]);
        }else{
            outputs[0] = sub(inputs[1], inputs[0]);
        }

        return outputs;
    }

    //TODO: Handle overflow (maxSum = 0xff)
    //TODO: Determine subtraction argument ordering (if it even matters)
    //TODO: Set flags for c, z, v, and n (also figure out what that means)
    public static int add(int x, int y){
        int sum = x + y;
        return sum;
    }

    private int sub(int x, int y){

        int diff = x - y;
        return diff;
    }

}
