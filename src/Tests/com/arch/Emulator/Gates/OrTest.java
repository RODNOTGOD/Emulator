package com.arch.Emulator.Gates;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class OrTest {

    Or testOrGate;

    @Test
    public void testTrue() {
        int[] input = {1, 0};
        int[] expected = {1};
        testOrGate = new Or();
        testOrGate.loadArguments(input);
        assertArrayEquals(expected, testOrGate.calculate());

        input[0] = 0;
        input[1] = 0;
        expected[0] = 0;
        testOrGate = new Or();
        testOrGate.loadArguments(input);
        assertArrayEquals(expected, testOrGate.calculate());

        input[0] = 14;
        input[1] = 13;
        expected[0] = 15;
        testOrGate = new Or();
        testOrGate.loadArguments(input);
        assertArrayEquals(expected, testOrGate.calculate());
    }

}