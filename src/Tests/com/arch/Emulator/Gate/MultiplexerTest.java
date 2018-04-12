package com.arch.Emulator.Gate;

import org.junit.Test;
import java.util.Arrays;

import java.util.Random;

import static org.junit.Assert.*;

public class MultiplexerTest {

    private Multiplexer testMuxGate;

    @Test
    public void testTrue() {
        int[] inputs = {32, 48, 0, 0};
        int[] inputs2 = {32, 22, 12, 14, 16, 18, 20, 19};
        int numOfInputLines = inputs.length;
        int[] selector = {0, 0};
        int[] selector2;
        int expected = 32;
        testMuxGate = new Multiplexer(inputs.length);
        testMuxGate.loadArguments(inputs);
        testMuxGate.setSelector(selector);
        assertEquals(expected, testMuxGate.calculate()[0]);

        numOfInputLines = 4;
        selector[0] = 0;
        selector[1] = 1;
        expected = 48;
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(inputs);
        testMuxGate.setSelector(selector);
        assertEquals(expected, testMuxGate.calculate()[0]);

        numOfInputLines = 8;
        selector2 = new int[3];
        selector2[0] = 0;
        selector2[1] = 1;
        selector2[2] = 1;
        expected = 14;
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(inputs2);
        testMuxGate.setSelector(selector2);
        assertEquals(expected, testMuxGate.calculate()[0]);

    }

}