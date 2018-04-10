package com.arch.Emulator;

import java.io.*;
import java.util.Iterator;

public class OpcodeReader implements Iterable<Integer>{

    FileReader opcodes = null;

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

    @Override
    public Iterator<Integer> iterator() {
        return new OpcodeIterator(opcodes);
    }

    private class OpcodeIterator implements Iterator<Integer> {

        private BufferedReader reader = null;
        private String line = null;
        private Integer opcode;

        public OpcodeIterator (FileReader opcodes) {
            reader = new BufferedReader(opcodes);
        }

        @Override
        public boolean hasNext() {
            try {
                line = reader.readLine();
                if (isComment(line))
                    line = null;
                return line != null;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public Integer next() {
            opcode = Integer.parseInt(line, 16);
            return opcode;
        }

        private boolean isComment(String line) {
            return !(line != null && line.matches("[-e]?\\d*\\.?\\d+"));
        }

    }
}
