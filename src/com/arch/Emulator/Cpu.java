package com.arch.Emulator;

import com.arch.Emulator.Gates.Demultiplexer;
import com.arch.Emulator.Gates.Multiplexer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Cpu {

    // Main Memory -> ram
    private Memory memory;

    // Registers
    private Register U10;
    private Register U11;
    private Register U12;
    private Register U13;

    // All used integers
    private int[] flags;

    private int instructionPointer;
    private int stackPointer;

    private int src;
    private int dst;


    // Main chips
    Multiplexer U111;
    Multiplexer U112;
    Multiplexer U113;
    Multiplexer U115;
    Multiplexer U116;
    Multiplexer U117;
    Multiplexer U118A;
    Multiplexer U118B;
    Multiplexer U120;
    Multiplexer U220;

    Demultiplexer U114;

    public Cpu() {
        flags = new int[4];
        U10 = new Register();
        U11 = new Register();
        U12 = new Register();
        U13 = new Register();

        U111 = new Multiplexer(8);
        U112 = new Multiplexer(8);
        U113 = new Multiplexer(8);
        U115 = new Multiplexer(4);
        U116 = new Multiplexer(4);
        U117 = new Multiplexer(2);
        U118A = new Multiplexer(8);
        U118B = new Multiplexer(8);
        U120 = new Multiplexer(2);
        U220 = new Multiplexer(4);

        startup();
    }

    /**
     * Simulates the startup period for the cpu
     *
     * <p>
     *     Reads from the memory location into the eprom at specified
     *     location. Runs a for-loop to do a small startup time
     * <p/>
     */
    private void startup() {
        for (int i = 0; i <= 100; i++); // no-op
        Random random = new Random();
        memory = new Memory();
        U10.setData(random.nextInt(0xFF));
        U11.setData(random.nextInt(0xFF));
        U12.setData(random.nextInt(0xFF));
        U13.setData(random.nextInt(0xFF));
    }

    /**
     *
     */
    public void run() {
        instructionPointer = 0x4000; // Default starting location
        stackPointer = 0x1000;
        while (instructionPointer < memory.getEndOfProgram()) {
            int opcode = 0;

            try {
                opcode = readMemory();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            instructionPointer += 4; // using fixed size of 4 byte instructions
            executeInstruction(opcode);
        }
    }

    /**
     *
     * @return
     * @throws IllegalAccessException
     */
    private int readMemory() throws IllegalAccessException {
        int opcodes = 0;
        for (int i = 0; i < 4; i++) // read four bytes from ram
            opcodes |= memory.fetch(instructionPointer + i) << (i * 8);
        return opcodes;
    }

    /**
     *
     * @param instruction
     */
    private void executeInstruction(int instruction) {
        int opcode = (instruction >>> 24);
        applyModifer(opcode, instruction);
        switch (opcode >>> 4) {
            case 0x8: // Mov
                System.out.println("Mov not implemented");
                break;
            case 0x1: // Addc
                System.out.println("Addc not implemented");
                break;
            case 0x2: // Subb
                System.out.println("Subb not implemented");
                break;
            case 0x3: // Cmp
                System.out.println("Cmp not implemented");
                break;
            case 0x4: // Not
                System.out.println("Not not implemented");
                break;
            case 0x5: // And
                System.out.println("And not implemented");
                break;
            case 0x6: // Or
                System.out.println("Or not implemented");
                break;
            case 0x7: // Xor
                System.out.println("Xor not implemented");
                break;
            case 0xB: // Jmp
                System.out.println("Jmp not implemented");
                break;
            case 0xD: // Conditional Jmp
                System.out.println("Cond. Jmp not implemented");
                break;
            case 0xE: // Nop
                System.out.println("No-op");
                break;
            default:
                throw new IllegalArgumentException("Unknown opcode " + Integer.toHexString(opcode) + " passed");
        }
    }

    /**
     *
     * @param modifier
     * @param instruction
     */
    private void applyModifer(int modifier, int instruction) {
        switch (modifier & 0xf) {
            case 0x0: // Reg to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 18) & 0b11;
                /*
                 * Control lines:
                 *      instruction pointer += size of instruction (4 byte)
                 *      ignore memory addressing
                 *      PERFORM ACTION
                 *      set MUX(u112) to sel(SRC)
                 *      send to data and set u118_A to sel(0)
                 *      set DEMUX(u114) to sel_A(DSR)
                 */
                break;
            case 0x1: // Immediate to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 16) & 0b1111;
                /*
                 * Control lines:
                 *      instruction pointer += size of instruction
                 *      ignore memory addressing
                 *      PREFORM ACTION
                 *
                 */
                break;
            case 0x2: // Mem loc to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 4) & 0xFFFF;
                break;
            case 0x3: // Reg to Mem loc
                dst = (instruction >>> 8) & 0xFFFF;
                src = (instruction >>> 6) & 0b11;
                break;
            case 0x8:
                break;
            case 0x9:
                break;
        }
    }

    /**
     *
     * @return
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     *
     * @return
     */
    public String dumpRegs() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reg0(U10): 0x").append(Integer.toHexString(U10.getData()).toUpperCase()).append("\n");
        sb.append("Reg1(U11): 0x").append(Integer.toHexString(U11.getData()).toUpperCase()).append("\n");
        sb.append("Reg2(U12): 0x").append(Integer.toHexString(U12.getData()).toUpperCase()).append("\n");
        sb.append("Reg3(U13): 0x").append(Integer.toHexString(U13.getData()).toUpperCase()).append("\n");
        return sb.toString();
    }
}
