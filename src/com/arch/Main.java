package com.arch;

import com.arch.Emulator.Cpu;
import com.arch.Emulator.OpcodeReader;
import com.arch.Emulator.Register;

public class Main {

    public static void main(String[] args) {

        Cpu cpu = new Cpu();
        OpcodeReader loader = new OpcodeReader("input.txt");

        /*
         * This section here we are going to read the hex/opcodes into the decoder
         * We will just read a string and just read into a integer and pass it to the decoder
         */

        try {
            loader.loadMemory(cpu.getMemory());
            loader.loadProgram();
            cpu.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(99);
        }

        // Dump out memory at end of program
        // Mostly just a debugging tool
        System.out.println(cpu.getMemory());
        System.out.println(cpu.dumpRegs());
    }

}
