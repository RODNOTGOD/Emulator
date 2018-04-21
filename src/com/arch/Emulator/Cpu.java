package com.arch.Emulator;

import com.arch.Emulator.Gates.*;

import java.lang.reflect.Array;
import java.util.*;

public class Cpu {

    // Main Memory -> ram
    private Memory memory;

    // Registers
    private Register U10;
    private Register U11;
    private Register U12;
    private Register U13;

    // All used integers
    private int flags;

    private int instructionPointer;
    private int stackPointer;

    private int src;
    private int dst;

    private int memoryline;
    private int dataline8;
    private int dataline16;
    private int ALU;

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

    // ALU Chips
    AddSub U100;
    And U101;
    Or  U102;
    Xor U103;
    Not U104;


    public Cpu() {
        flags = 0b000;
        U10 = new Register();   // Register 0
        U11 = new Register();   // Register 1
        U12 = new Register();   // Register 2
        U13 = new Register();   // Register 3

        U111 = new Multiplexer(8);  // 8-1 Mux after logic gates
        U112 = new Multiplexer(8);  // 8-1 Mux after Registers
        U113 = new Multiplexer(8);  // 8-1 Mux after Registers
        U115 = new Multiplexer(4);  // 4-1 Mux feeds into IP
        U116 = new Multiplexer(4);  // 4-1 Mux from IR to Memory
        U117 = new Multiplexer(2);  // 2-1 Mux feeds into SP
        U118A = new Multiplexer(8); // 8-1 Mux feeds into U114 demux
        U118B = new Multiplexer(8); // 8-1 Mux feeds into U114 demux
        U120 = new Multiplexer(2);  // 2-1 Mux feeds into flags
        U220 = new Multiplexer(4);  // 4-1 Mux feeds into memory

        U114 = new Demultiplexer(2); // 2-4 Demux feeds into registers

        U100 = new AddSub();
        U101 = new And();
        U102 = new Or();
        U103 = new Xor();
        U104 = new Not();

        dataline8 = 0;
        dataline16 = 0;

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
        instructionPointer = 0xF000; // Default starting location
        stackPointer = 0xE000;
        while (instructionPointer < memory.getEndOfProgram()) {
            int opcode = 0;

            try {
                opcode = readMemory();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//            instructionPointer += 4; // using fixed size of 4 byte instructions
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
            opcodes |= memory.secureFetch(instructionPointer + i) << (i * 8);
        return opcodes;
    }

    /**
     *
     * @param instruction
     */
    private void executeInstruction(int instruction) {
        loadSettings(instruction);
        runInstructionSettings();
    }

    private void loadSettings(int fullOpcode) {
        int[] input;
        int opcode = (fullOpcode >>> 24);
        applyModifer(opcode, fullOpcode);
        switch (opcode >>> 4) {
            case 0x8: // Mov
                int[] selectors = new int[]{1, 0};
                U115.setInputSelector(selectors);

                selectors = new int[]{0, 1};
                U114.setInputSelector(selectors);

                selectors = convertToBinaryArray(dst);
                U114.setOutputSelector(selectors);

                System.out.println("dst: " + dst + "\nsrc: " + src);
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
        int[] control;
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
                control = new int[] {1, 0};
                U115.setInputSelector(control);

                control = new int[] {1, 0};
                U116.setInputSelector(control);

                // FIXME(Stefen Ramirez): Find out when todo memory reads/writes
//                control = new int[] {1, 0};
//                U220.setInputSelector(control);

                control = convertToBinaryArray(src);
                U112.setInputSelector(control);

                control = convertToBinaryArray(src);
                U113.setInputSelector(control);

                control = new int[]{0, 0, 0};
                U111.setInputSelector(control);

                control = new int[]{0, 0, 0};
                U118A.setInputSelector(control);

                control = new int[]{0, 0, 0};
                U118B.setInputSelector(control);

                control = convertToBinaryArray(dst);
                U114.setInputSelector(control);

                control = new int[]{0, 0};
                U220.setInputSelector(control);
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
                control = new int[] {1, 0};
                U115.setInputSelector(control);

                control = new int[] {1, 0};
                U116.setInputSelector(control);

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

    private void runInstructionSettings() {
        // TODO(Stefen Ramirez): Find out how to calculate IP relative
        int[] input;

        // Data, Inst, IPinc, IPrel
        input = new int[] {(dataline16 << 8) | dataline8, dst, AddSub.add(instructionPointer, 4), 0};
        U115.loadArguments(input);
        instructionPointer = U115.calculate()[0];

        // IP, SP, INST, DATA
        input = new int[] {instructionPointer, stackPointer, dst, (dataline16 << 8) | dataline8};
        U116.loadArguments(input);
        U116.calculate();

        // Only if writing to memory
        input = new int[] {dataline8, dataline16, ALU, dst};
        U220.loadArguments(input);
        U220.calculate();

        // Load into mux
        input = new int[] {U10.getData(), U11.getData(), U12.getData(), U13.getData(), memoryline, flags, instructionPointer, 0};
        U112.loadArguments(input);
        U112.calculate();
        dataline8 = U112.transmit()[0];

        // Load into mux
        input = new int[] {U10.getData(), U11.getData(), U12.getData(), U13.getData(), memoryline, flags, instructionPointer, 0};
        U113.loadArguments(input);
        U113.calculate();
        dataline16 = U113.transmit()[0];

        // ALU SECTION
        // Add/Sub
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U100.loadArguments(input);
        U100.calculate();

        // And
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U101.loadArguments(input);
        // Or
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U102.loadArguments(input);
        U102.calculate();

        // Xor
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U103.loadArguments(input);
        U103.calculate();

        // Not
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U104.loadArguments(input);
        U104.calculate();

        input = new int []{U101.transmit()[0], U101.transmit()[0], U102.transmit()[0], U103.transmit()[0], U104.transmit()[0], 0, 0, 0};
        U111.loadArguments(input);
        ALU = U111.calculate()[0];

        input = new int []{dataline8, stackPointer, dst, ALU, 0, memoryline, 0, 0};
        U118A.loadArguments(input);
        U118A.calculate();

        input = new int []{dataline16, stackPointer, dst, ALU, 0, memoryline, 0, 0};
        U118B.loadArguments(input);
        U118B.calculate();

        input = new int[]{U118A.calculate()[0], U118B.calculate()[0]};
        U114.loadArguments(input);
        U114.calculate();
        writeRegisters();
    }

    private void writeRegisters() {
        switch (dst) {
            case 0:
                U10.setData(U114.transmit()[dst]);
                break;
            case 1:
                U11.setData(U114.transmit()[dst]);
                break;
            case 2:
                U12.setData(U114.transmit()[dst]);
                break;
            case 3:
                U13.setData(U114.transmit()[dst]);
                break;
            default:
                System.err.println("Illegal register");
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

    private int[] convertToBinaryArray(int number) {
        int arrayLength = (int) Math.floor(Math.log(number) / Math.log(2)) + 1;
        if (arrayLength == 0)
            arrayLength = 1;
        int[] convertArray = new int[arrayLength];
        for (int i = 0; number != 0; i++) {
            convertArray[i] = number % 2;
            number >>>= 1;
        }
        reverse(convertArray);
        return convertArray;
    }

    private static void reverse(int[] data) {
        int left = 0;
        int right = data.length - 1;

        while( left < right ) {
            // swap the values at the left and right indices
            int temp = data[left];
            data[left] = data[right];
            data[right] = temp;

            // move the left and right index pointers in toward the center
            left++;
            right--;
        }
    }
}
