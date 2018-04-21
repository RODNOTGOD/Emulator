package com.arch.Emulator.Gates;

import org.junit.Test;

import static org.junit.Assert.*;

public class XorTest {

    Xor testXorGate;

    @Test
    public void testTrue() {
        testXorGate = new Xor();
        int[] input = {8, 1};
        int[] expected = {9};
        testXorGate.loadArguments(input);
        assertArrayEquals(expected, testXorGate.calculate());

        input[0] = 0;
        input[1] = 0;
        expected[0] = 0;
        testXorGate = new Xor();
        testXorGate.loadArguments(input);
        assertArrayEquals(expected, testXorGate.calculate());

        input[0] = 1;
        input[1] = 0;
        expected[0] = 1;
        testXorGate = new Xor();
        testXorGate.loadArguments(input);
        assertArrayEquals(expected, testXorGate.calculate());

        input[0] = 0;
        input[1] = 1;
        expected[0] = 1;
        testXorGate = new Xor();
        testXorGate.loadArguments(input);
        assertArrayEquals(expected, testXorGate.calculate());

        input[0] = 10;
        input[1] = 5;
        expected[0] = 15;
        testXorGate = new Xor();
        testXorGate.loadArguments(input);
        assertArrayEquals(expected, testXorGate.calculate());
    }
}