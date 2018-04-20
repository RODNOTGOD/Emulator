package com.arch.Emulator.Gates;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class AddSubTest {
    private AddSub testAddSub;

    @Test
    public void testTrue(){
        AddSub addsubTest;
        int[] inputs = {3, 6};
        int[] expected = {9};
        int[] inputSelector = {0};
        int[] outputSelector = {0};
        int[] result;

        //Add Test
        addsubTest = new AddSub();
        addsubTest.loadArguments(inputs);
        addsubTest.setInputSelector(inputSelector);
        addsubTest.setOutputSelector(outputSelector);
        result = addsubTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

        //Subtract Test
        inputSelector[0] = 1;
        inputs[0] = 13;
        expected[0] = 7;
        addsubTest.loadArguments(inputs);
        addsubTest.setInputSelector(inputSelector);
        addsubTest.setOutputSelector(outputSelector);
        result = addsubTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

    }
}