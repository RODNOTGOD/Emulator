package com.arch.Emulator;

public class Enumlator {

    /*
     * private Memory memory
     * private ControlLine controller;
     */

    public Enumlator () {
        /*
         * memory = new MemoryAllocator(AmountOfMemory);
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
        for (int i = 0; i < 100; i++);
    }

    public void read(int opcode) {
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
    }
}
