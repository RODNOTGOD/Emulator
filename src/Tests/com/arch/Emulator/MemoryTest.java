package com.arch.Emulator;

import org.junit.Test;

import javax.swing.plaf.metal.MetalMenuBarUI;

import static org.junit.Assert.*;

public class MemoryTest {

    @Test
    public void CheckWriteAndFetch() throws IllegalAccessException {
        Memory memory = new Memory();
        memory.write(0x1000, 74);
        int data = memory.fetch(0x1000);
        assertEquals(data, 74);
    }

    @Test(expected = IllegalAccessException.class)
    public void CheckBadWrite() throws IllegalAccessException {
        Memory memory = new Memory();
        memory.write(0x4000, 0x0);
        memory.fetch(0x1000);
    }

    @Test(expected = IllegalAccessException.class)
    public void CheckBadFetch() throws IllegalAccessException {
        Memory memory = new Memory();
        memory.fetch(0xF000);
    }
}