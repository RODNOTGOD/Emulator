package com.arch.Emulator;

public class Enumlator {

    /*
    private Memory memory
    private ControlLine controller;
     */

    public Enumlator () {
        /*
        memory = new MemoryLocator();
        controller = new ControlLine();
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
        Read the location specified
        Move random values into the registers
         */
        for (int i = 0; i < 100; i++);
    }
}
