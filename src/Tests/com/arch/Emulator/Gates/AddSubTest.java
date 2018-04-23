package com.arch.Emulator.Gates;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AddSubTest {

    @Test
    public void testTrue(){
        AddSub addsubTest;
        int[] inputs = {3, 6};
        int[] expected = {9};
        int[] result;


        //Add Test
        addsubTest = new AddSub();
        addsubTest.setAdder();
        addsubTest.loadArguments(inputs);
        result = addsubTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

        //Subtract Test
        addsubTest.setSubber();
        inputs[1] = 13;
        expected[0] = Math.abs(inputs[0] - inputs[1]);
        addsubTest.loadArguments(inputs);
        result = addsubTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

    }
}