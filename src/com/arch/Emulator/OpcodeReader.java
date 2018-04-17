package com.arch.Emulator;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String line;
        Matcher matcher;
        Pattern pattern = Pattern.compile("^([\\d|ABCDEF]+)");
        BufferedReader reader = new BufferedReader(opcodes);
        while ((line = reader.readLine()) != null) {
            line = line.toUpperCase();
            matcher = pattern.matcher(line);
            if (!matcher.find())
                continue;
            int parsed = Integer.parseUnsignedInt(matcher.group(1), 16);
            int fullOpcode = parsed;
            int byteLength;
            for (byteLength = 8; byteLength > 0 && parsed != 0; byteLength--)
                parsed >>= 4;
            if (byteLength != 0)
                fullOpcode <<= byteLength * 4;
            prgMemory.loadInstruction(fullOpcode);
        }
    }
}
