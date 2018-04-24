package com.arch;

import com.arch.Emulator.Cpu;
import com.arch.Emulator.Gates.AddSub;
import com.arch.Emulator.OpcodeReader;
import com.arch.Emulator.Register;

public class Main {

    public static void main(String[] args) {

        Cpu cpu = new Cpu();
        OpcodeReader loader = new OpcodeReader("input.txt");

        try {
            loader.loadMemory(cpu.getMemory());
            loader.loadProgram();
            cpu.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Dump out memory at end of program
            // Mostly just a debugging tool
            System.out.println(cpu.getMemory());
            System.out.println(cpu.dumpRegs());
            System.out.println(cpu.dumpFlags());
        }
    }

}
