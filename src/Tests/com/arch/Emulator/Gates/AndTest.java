package com.arch.Emulator.Gates;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class AndTest {

    private And testAndGate;

    @Test
    public void testTrue() {
        int[] input = {1, 1};
        int[] expected = {1};
        testAndGate = new And();
        testAndGate.loadArguments(input);
        assertArrayEquals(expected, testAndGate.calculate());
    }

    @Test
    public void testFalse() {
        Random random = new Random();
        int[] unexpected = {0};
        int[] randomArray = new int[2];
        for (int j = 0; j < 10000; j++) {
            randomArray[0] = 0;
            for (int i = 1; i < randomArray.length; i++)
                randomArray[i] = random.nextInt(9) + 1;
            testAndGate = new And();
            testAndGate.loadArguments(randomArray);
            assertArrayEquals(unexpected, testAndGate.calculate());
        }
    }

    @Test
    public void testNonBinary() {
        Random random = new Random();
        int[] set = {random.nextInt(10), random.nextInt(10)};
        int result = set[0];
        for (int item : set)
            result &= item;
        testAndGate = new And();
        testAndGate.loadArguments(set);
        assertEquals(result, testAndGate.calculate()[0]);
    }
}