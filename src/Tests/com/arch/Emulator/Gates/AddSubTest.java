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

        //Select from Line 1 and pass data to Line 1
        addsubTest = new AddSub();
        addsubTest.loadArguments(inputs);
        addsubTest.setInputSelector(inputSelector);
        addsubTest.setOutputSelector(outputSelector);
        result = addsubTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

    }
}