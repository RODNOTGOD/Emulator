package com.arch;

import com.arch.Emulator.Enumlator;
import com.arch.Emulator.OpcodeReader;

public class Main {

    public static void main(String[] args) {

        Enumlator enumlator = new Enumlator();
        OpcodeReader reader = new OpcodeReader("input.txt");

        /*
         * This section here we are going to read the hex/opcodes into the decoder
         * We will just read a string and just read into a integer and pass it to the decoder
         */
        for (int opcode : reader) {
            enumlator.read(opcode);
        }

    }

}
