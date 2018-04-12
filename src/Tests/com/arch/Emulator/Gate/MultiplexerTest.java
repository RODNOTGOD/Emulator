package com.arch.Emulator.Gate;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MultiplexerTest {

    private Multiplexer testMuxGate;

    @Test
    public void testTrue() {
        int numOfInputLines = 4;
        int[] selector = {1, 0};
        int[] expected = {2};
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(selector);
        assertArrayEquals(expected, testMuxGate.transmit());
    }

}