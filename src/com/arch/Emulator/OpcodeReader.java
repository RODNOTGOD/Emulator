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

    private static boolean isComment(String line) {
        return !(line != null && line.matches("[-+]?\\d*\\.?\\d+"));
    }

    @Override
    public Iterator<Integer> iterator() {
        return new OpcodeIterator(opcodes);
    }

    private class OpcodeIterator implements Iterator<Integer> {

        private BufferedReader reader = null;
        private String line = null;

        public OpcodeIterator (FileReader opcodes) {
            reader = new BufferedReader(opcodes);
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Integer next() {
            try {
                line = reader.readLine();
                return Integer.parseInt(line);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
