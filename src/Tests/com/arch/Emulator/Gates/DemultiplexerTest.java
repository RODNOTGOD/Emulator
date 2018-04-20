package com.arch.Emulator.Gates;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class DemultiplexerTest {

    @Test
    public void testTrue() {
        int[] expected = {0, 32, 0, 0};
        int[] inputs = {32};
        int[] inputSelector = {0, 1};
        int[] result;

        //Select from Line 1 and pass data to Line 1
        Demultiplexer demuxTest = new Demultiplexer(1);
        demuxTest.enabled();
        demuxTest.loadArguments(inputs);
        demuxTest.setInputSelector(inputSelector);
        result = demuxTest.calculate();
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expected, result);

    }

}