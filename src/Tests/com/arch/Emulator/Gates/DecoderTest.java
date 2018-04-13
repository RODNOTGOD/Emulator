package com.arch.Emulator.Gates;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class DecoderTest {
    @Test
    public void CheckDecoding() {
        int[] input = {1, 0, 0};
        Decoder decoder = new Decoder(input.length);
        decoder.loadArguments(input);
        assertEquals(decoder.calculate()[3], 1);
    }

    @Test
    public void CheckRandomDecoding() {
        Random random = new Random();
        int[] inputs = new int[random.nextInt(3) + 1];
        Decoder decoder = new Decoder(inputs.length);
        int decodedValue = 0;
        for (int i = 0, power = inputs.length - 1 ; i < inputs.length; i++, power--) {
            inputs[i] = random.nextInt(2);
            decodedValue += inputs[i] * Math.pow(2, power);
        }
        decoder.loadArguments(inputs);
        decoder.calculate();
        assertEquals(decoder.transmit()[decoder.transmit().length - decodedValue - 1], 1);

    }
}