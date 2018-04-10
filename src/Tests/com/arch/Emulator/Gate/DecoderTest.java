package com.arch.Emulator.Gate;

import org.junit.Test;

import static org.junit.Assert.*;

public class DecoderTest {

    Decoder decoder;

    @Test (expected = IllegalArgumentException.class)
    public void testBadArguments() {
        decoder = new Decoder(2, 3);
    }
}