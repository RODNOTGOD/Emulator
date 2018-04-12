package com.arch.Emulator;

import java.io.*;
import java.util.Iterator;

public class OpcodeReader {

    private FileReader opcodes = null;
    private Memory prgMemory = null;

    /**
     * Reads a file containing opcodes/comments for the emulator
     *
     * @param filename file to read
     */
    public OpcodeReader(String filename) {
        opcodes = null;
        try {
            opcodes = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadMemory(Memory memory) {
        this.prgMemory = memory;
    }

    public void loadProgram() throws Exception {
        if (prgMemory == null) throw new AssertionError();
        String line = null;
        BufferedReader reader = new BufferedReader(opcodes);
        while ((line = reader.readLine()) != null) {
            prgMemory.loadInstruction(Integer.parseUnsignedInt(line, 16));
        }
    }
}
