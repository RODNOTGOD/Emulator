package com.arch;

import com.arch.Emulator.Cpu;
import com.arch.Emulator.Gate.Multiplexer;
import com.arch.Emulator.OpcodeReader;

public class Main {

    public static void main(String[] args) {

        Cpu cpu = new Cpu();
        OpcodeReader loader = new OpcodeReader("input.txt");

        /*
         * This section here we are going to read the hex/opcodes into the decoder
         * We will just read a string and just read into a integer and pass it to the decoder
         */
        //for (int opcode : reader) {
        //    emulator.read(opcode);
        //}

        try {
            loader.loadMemory(cpu.getMemory());
            loader.loadProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cpu.run();

        // Dump out memory at end of program
        // Mostly just a debugging tool
        System.out.println(cpu.getMemory());
    }

}
