package com.arch.Emulator;

import org.omg.PortableInterceptor.INACTIVE;
//import sun.rmi.server.InactiveGroupException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Memory {

    private char[][] ram;
    private int programLocation = 0;
    private int endOfProgram = 0;

    Memory() {
        ram = new char[16][];
        // 16 bit ram
        for (int i = 0; i < 16; i++) {
            ram[i] = new char[0x1000];
        }
    }

    public void loadInstruction(int opcode) {
        opcode = Integer.reverseBytes(opcode);
        if (programLocation == 0)
            programLocation = 0x4000;
        while (opcode != 0) {
            ram[(programLocation & 0xFFFF) >> 12][programLocation & 0xFFF] = (char) (opcode & 0xFF);
            opcode >>= 8;
            programLocation += 1;
        }
        endOfProgram = programLocation;
    }

    public int getEndOfProgram() {
        return endOfProgram;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ram.length; i++) {
            sb.append("SECTION " );
            if (i == 14) sb.append("I/O  ");
            else if (i == 15) sb.append("EPROM");
            else sb.append("DATA ");
            sb.append(": [ $").append(Integer.toHexString(i << 12)).append(" ]");
            for (int j = 0; j < ram[i].length; j++) {
                sb.append(" ").append(Integer.toHexString(ram[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int fetch(int instructionPointer) {
        return ram[(instructionPointer & 0xFFFF) >> 12][instructionPointer & 0xFFF];
    }
}
