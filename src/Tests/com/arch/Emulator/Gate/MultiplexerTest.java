package com.arch.Emulator.Gate;

import org.junit.Test;
import java.util.Arrays;

import java.util.Random;

import static org.junit.Assert.*;

public class MultiplexerTest {

    private Multiplexer testMuxGate;

    @Test
    public void testTrue() {
        int numOfInputLines = 4;
        int[] selector = {0, 0};
        int[] selector2 = new int[4];
        int expected = 0;
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(selector);
        assertEquals(expected, testMuxGate.calculate()[0]);

        numOfInputLines = 4;
        selector[0] = 0;
        selector[1] = 1;
        expected = 1;
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(selector);
        assertEquals(expected, testMuxGate.calculate()[0]);

        numOfInputLines = 16;
        selector2[0] = 0;
        selector2[1] = 1;
        selector2[2] = 0;
        selector2[3] = 1;
        expected = 5;
        testMuxGate = new Multiplexer(numOfInputLines);
        testMuxGate.loadArguments(selector2);
        assertEquals(expected, testMuxGate.calculate()[0]);

    }

}