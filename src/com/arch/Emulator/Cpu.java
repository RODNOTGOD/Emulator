package com.arch.Emulator;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Cpu {

    private Memory memory;

    private int instructionPointer;

    /*
     * private Memory memory
     * private ControlLine controller;
     */

    public Cpu() {
        /*
         * controller = new ControlLine();
         */
        startup();
    }

    /**
     * @Brief: Simulates the startup period for the cpu
     *
     *      Reads from the memory location into the eprom at specified
     *      location. Runs a for-loop to do a small startup time
     */
    private void startup() {
        /*
         * Plan:
         *      Read the location specified
         *      Move random values into the registers
         *
         * Comments:
         *      Need to load all memory first and select a section of EPROM to read into.
         *      This section is just a random hex location in the EPROM section of the memory
         */
        for (int i = 0; i < 100; i++); // no-op
        memory = new Memory();
    }

    public void run() {
        /*
         * Plan:
         *      Read each instruction give to decoder to set all control lines. This just push data through
         *      the registers/circuits.
         *
         * Future:
         *      Move to an array implementation to be able to jump back to previous instructions.
         *
         * Comments:
         *      If we don't implement the jmp routines then we don't have problem with this current
         *      implmenetation. But if we plan on jumping then we to have a method to jump back to a previous
         *      instruction.
         *
         *      Probably should move to a loaded array with the opcodes. This Would allow easier travel
         *      back of instructions
         */
        instructionPointer = 0x4000; // Default starting location
        while (instructionPointer != memory.getEndOfProgram()) {
            int opcodes = 0;
            try {
                opcodes = readMemory();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println("Instruction read: " + Integer.toHexString(opcodes));
        }
    }

    private int readMemory() throws IllegalAccessException {
        int opcodes = 0;
        for (int i = 0; i < 4; i++) { // read four bytes from ram
            opcodes |= memory.fetch(instructionPointer++);
            opcodes <<= 8;
        }
        return opcodes;
    }

    private void loadInstruction(int instruction) {
        switch (instruction) {
            case 0x80:
                System.out.println("Moving 2 8-bit register");
                break;
            default:
                throw new IllegalArgumentException("Unknown instruction " + Integer.toHexString(instruction) + " passed");
        }
    }

    public Memory getMemory() {
        return memory;
    }
}
