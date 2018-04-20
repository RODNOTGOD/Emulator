package com.arch.Emulator.Gates;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class DemultiplexerTest {
    private Demultiplexer demuxTest;

    @Test
    public void testTrue(){
        int[] expected = {0, 32, 0, 0};
        int[] inputs = {2, 32};
        int[] inputSelector = {1};
        int[] outputSelector = {0, 1};
        int[] result;

        //Select from Line 1 and pass data to Line 1
        demuxTest = new Demultiplexer(inputs.length);
        demuxTest.loadArguments(inputs);
        demuxTest.setInputSelector(inputSelector);
        demuxTest.setOutputSelector(outputSelector);
        result = demuxTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

        //Select from Line 0 and pass data to Line 1
        inputSelector[0] = 0;
        expected[1] = inputs[0];
        //demuxTest.loadArguments(inputs);
        demuxTest.setInputSelector(inputSelector);
        result = demuxTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

        // Select from Line 0 and pass to Line 3
        outputSelector[0] = 1;
        outputSelector[1] = 1;
        expected[1] = 0;
        expected[3] = 2;
        demuxTest.loadArguments(inputs);
        demuxTest.setOutputSelector(outputSelector);
        result = demuxTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

    }

}