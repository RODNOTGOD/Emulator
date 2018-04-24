package com.arch.Emulator;

import com.arch.Emulator.Gates.*;

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
    // ZERO, OVERFLOW, N, CARRY
    private int flags;

    private int instructionPointer;
    private int stackPointer;

    private int src;
    private int dst;
    private int offset;

    private int offsetLine;
    private int instructionLine;
    private int memoryline;
    private int dataline8;
    private int dataline16;
    private int ALU;

    // Main chips
    private Multiplexer U111;
    private Multiplexer U112;
    private Multiplexer U113;
    private Multiplexer U115;
    private Multiplexer U116;
    private Multiplexer U117;
    private Multiplexer U118A;
    private Multiplexer U118B;
    private Multiplexer U120;
    private Multiplexer U220;

    private Demultiplexer U114A;
    private Demultiplexer U114B;

    // ALU Chips
    private AddSub U100;
    private And U101;
    private Or  U102;
    private Xor U103;
    private Not U104;


    public Cpu() {
        flags = 0b000;
        U10 = new Register();   // Reg 0
        U11 = new Register();   // Reg 1
        U12 = new Register();   // Reg 2
        U13 = new Register();   // Reg 3

        U111 = new Multiplexer(8);  // 8-1 Mux after ALU
        U112 = new Multiplexer(8);  // 8-1 Mux after registers
        U113 = new Multiplexer(8);  // 8-1 Mux after registers
        U115 = new Multiplexer(4);  // 4-1 Mux before IP
        U116 = new Multiplexer(4);  // 4-1 Mux into mem
        U117 = new Multiplexer(2);  // 2-1 Mux before SP
        U118A = new Multiplexer(8); // 8-1 Mux from lines
        U118B = new Multiplexer(8); // 8-1 Mux from lines
        U120 = new Multiplexer(2);  // 2-1 Mux to flags
        U220 = new Multiplexer(4);  // 4-1 Mux from lines to mem

        U114A = new Demultiplexer(1);   // "2-4" demux to registers
        U114B = new Demultiplexer(1);   // "2-4" demux to registers

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
                opcode = fetchInstruction();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            executeInstruction(opcode);
            int opcPrint = opcode << 4;
            //System.out.println("opcode: " + Integer.toHexString(opcPrint));
            System.out.println("IP: "+ Integer.toHexString(instructionPointer));
        }
    }


    /**
     * Reads 4 bytes of numbers for each fetch to program memory
     *
     * @return the full constructed opcode from little endian
     * @throws IllegalAccessException bad fetch of memory
     */
    private int fetchInstruction() throws IllegalAccessException {
        int opcodes = 0;
        for (int i = 0; i < 4; i++) // read four bytes from ram
            opcodes |= memory.secureFetch(instructionPointer + i) << (i * 8);
        return opcodes;
    }


    /**
     * Executes the opcode instruction sent to cpu
     *
     * Loads all settings to the specific instruction and
     * executes the opcode
     *
     * @param instruction opcode to execute
     */
    private void executeInstruction(int instruction) {
        loadSettings(instruction);
        runInstructionSettings();
        // XXX remove this debug
        System.out.println(dumpRegs());
    }


    /**
     *
     * @param fullOpcode
     */
    private void loadSettings(int fullOpcode) {
        int opcode = (fullOpcode >>> 24);
        applyModifer(opcode, fullOpcode);
        switch (opcode >>> 4) {
            case 0x8: // Mov
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // What to end if enabled to registers
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(0));
                U118B.setInputSelector(convertToBinaryArray(0));

                // Set flags
                U120.setInputSelector(convertToBinaryArray(1));
                break;

            case 0x1: // Addc
                U100.setAdder();
                U120.setInputSelector(convertToBinaryArray(0));

                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(0));

                // What to end if enabled to registers
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));
                U220.setInputSelector(convertToBinaryArray(2));
                break;

            case 0x2: // Subb
                U100.setSubber();
                U120.setInputSelector(convertToBinaryArray(0));

                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(0));

                // What to end if enabled to registers
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));
                U220.setInputSelector(convertToBinaryArray(2));
                break;
            case 0x3: // Cmp
                U100.setSubber();
                U120.setInputSelector(convertToBinaryArray(0));
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                U111.setInputSelector(convertToBinaryArray(0));

                // What to end if enabled to registers
                U114A.disabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(0));
                U118B.setInputSelector(convertToBinaryArray(0));
                U120.setInputSelector(convertToBinaryArray(0));
                break;
            case 0x4: // Not
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(4));

                // What to end if enabled to registers
                U114A.setInputSelector(convertToBinaryArray(dst));
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(1));
                U220.setInputSelector(convertToBinaryArray(2));
                break;
            case 0x5: // And
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(1));

                // What to end if enabled to registers
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(1));
                break;
            case 0x6: // Or
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(2));

                // What to end if enabled to registers
                U114A.setInputSelector(convertToBinaryArray(dst));
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(1));
                break;
            case 0x7: // Xor
                // Update the IP
                U115.setInputSelector(convertToBinaryArray(2));

                // Set the ALU
                U111.setInputSelector(convertToBinaryArray(3));

                // What to end if enabled to registers
                U114A.setInputSelector(convertToBinaryArray(dst));
                U114A.enabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(1));
                break;
            case 0xB: // Jmp
                // What to end if enabled to registers
                U114A.setInputSelector(convertToBinaryArray(0));
                U114A.disabled();
                U114B.disabled();

                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(1));
                break;
            case 0xD: // Conditional Jmp
                checkCondition(opcode);
                U114A.setInputSelector(convertToBinaryArray(0));
                U114A.disabled();
                U114B.disabled();

                dataline8 = 0;
                dataline16 = 0;
                offset = (fullOpcode >> 8) & 0xFFFF;
                U116.setInputSelector(convertToBinaryArray(3));
                // What to send to data lines
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));

                U120.setInputSelector(convertToBinaryArray(0));
                break;
            case 0xE: // Nop
                U120.setInputSelector(convertToBinaryArray(0));
                U118A.setInputSelector(convertToBinaryArray(3));
                U118B.setInputSelector(convertToBinaryArray(3));
                U114A.disabled();
                U114B.disabled();
                break;
            default:
                throw new IllegalArgumentException("Unknown opcode " + Integer.toHexString(opcode) + " passed");
        }
    }


    /**
     * Applies operand specific controls for data flow
     *
     * @param modifier operands to use
     * @param instruction instruction container for dst, src
     */
    private void applyModifer(int modifier, int instruction) {
        int[] control;
        switch (modifier & 0xf) {
            case 0x0: // Reg to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 18) & 0b11;

                U115.setInputSelector(convertToBinaryArray(2));
                U116.setInputSelector(convertToBinaryArray(2));

                U112.setInputSelector(convertToBinaryArray(src));
                U113.setInputSelector(convertToBinaryArray(dst));

                U111.setInputSelector(convertToBinaryArray(0));
                U220.setOff();

                U114A.setInputSelector(convertToBinaryArray(dst));
                U114A.enabled();
                U114B.disabled();

                U220.setInputSelector(convertToBinaryArray(0));
                U220.setOff();
                break;

            case 0x1: // Immediate to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 12) & 0xFF;

                instructionLine = src;

                U115.setInputSelector(convertToBinaryArray(2));

                U116.setInputSelector(convertToBinaryArray(2));

                U112.setInputSelector(convertToBinaryArray(6));

                U113.setInputSelector(convertToBinaryArray(dst));

                U111.setInputSelector(convertToBinaryArray(0));

                U114A.setInputSelector(convertToBinaryArray(dst));
                U114A.enabled();
                U114B.disabled();

                U220.setInputSelector(convertToBinaryArray(0));
                U220.setOff();
                break;

            case 0x2: // Mem loc to Reg
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 4) & 0xFFFF;

                U220.setRead(); // reading not writing memory
                instructionLine = src;

                U115.setInputSelector(convertToBinaryArray(2));
                U116.setInputSelector(convertToBinaryArray(2));
                U112.setInputSelector(convertToBinaryArray(4));
                U113.setInputSelector(convertToBinaryArray(dst));
                U111.setInputSelector(convertToBinaryArray(0));

                U114A.enabled();
                U114B.disabled();
                break;

            case 0x3: // Reg to Mem loc
                dst = (instruction >>> 8) & 0xFFFF;
                src = (instruction >>> 6) & 0b11;
                instructionLine = dst;

                U115.setInputSelector(convertToBinaryArray(2));
                U116.setInputSelector(convertToBinaryArray(2));
                U112.setInputSelector(convertToBinaryArray(src));
                U113.setInputSelector(convertToBinaryArray(4));
                U111.setInputSelector(convertToBinaryArray(0));

                U114A.disabled();
                U114B.disabled();

                U220.setInputSelector(convertToBinaryArray(0));
                U220.setReadWrite();
                break;

            case 0x8:
                dst = (instruction >>> 22) & 0b11;
                src = (instruction >>> 18) & 0b11;

                U115.setInputSelector(convertToBinaryArray(0));
                U116.setInputSelector(convertToBinaryArray(2));
                U112.setInputSelector(convertToBinaryArray(dst));
                U113.setInputSelector(convertToBinaryArray(src));
                U111.setInputSelector(convertToBinaryArray(0));

                U114A.disabled();
                U114B.disabled();

                U220.setInputSelector(convertToBinaryArray(0));
                U220.setOff();
                break;

            case 0x9:
                dst = (instruction >>> 16) & 0xFF;
                src = (instruction >>> 8) & 0xFF;
                instructionLine = dst << 8 | src;
                dataline8 = 0;
                dataline16 = 0;

                U115.setInputSelector(convertToBinaryArray(1));
                U116.setInputSelector(convertToBinaryArray(3));
                U112.setInputSelector(convertToBinaryArray(0));
                U113.setInputSelector(convertToBinaryArray(0));
                U111.setInputSelector(convertToBinaryArray(0));

                U114A.disabled();
                U114B.disabled();

                U220.setInputSelector(convertToBinaryArray(0));
                U220.setOff();
                break;
        }
    }


    /**
     *
     */
    private void runInstructionSettings() {
        int[] input;

        // Data, Inst, IPinc, IPrel
        offsetLine = AddSub.add(AddSub.add(instructionPointer, 4), offset);

        // Set address access location to memory
        input = new int[] {instructionPointer, stackPointer, instructionLine, (dataline8 << 8) | dataline16};
        U116.loadArguments(input);
        U116.calculate();
        memoryline = readMemory();

        input = new int[] {dataline8, dataline16, ALU, dst};
        U220.loadArguments(input);
        U220.calculate();

        // Load into mux
        input = new int[] {U10.getData(), U11.getData(), U12.getData(), U13.getData(), memoryline, flags, instructionLine, 0};
        U112.loadArguments(input);
        U112.calculate();
        dataline8 = U112.transmit()[0];

        // Load into mux
        input = new int[] {U10.getData(), U11.getData(), U12.getData(), U13.getData(), memoryline, flags, instructionLine, 0};
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
        U101.calculate();

        // Or
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U102.loadArguments(input);
        U102.calculate();

        // Xor
        input = new int []{U112.transmit()[0], U113.transmit()[0]};
        U103.loadArguments(input);
        U103.calculate();

        // Not
        input = new int []{U113.transmit()[0]};
        U104.loadArguments(input);
        U104.calculate();

        // Set ALU
        input = new int []{U100.transmit()[0], U101.transmit()[0], U102.transmit()[0], U103.transmit()[0], U104.transmit()[0], 0, 0, 0};
        U111.loadArguments(input);
        ALU = U111.calculate()[0];

        U120.loadArguments(new int[]{U100.getFlags(), ALU});
        flags = U120.calculate()[0] & 0xF;

        input = new int []{dataline8, stackPointer, dst, ALU, 0, memoryline, 0, 0};
        U118A.loadArguments(input);
        U118A.calculate();

        input = new int []{dataline16, stackPointer, dst, ALU, 0, memoryline, 0, 0};
        U118B.loadArguments(input);
        U118B.calculate();

        input = new int[] {dataline8 << 8 | dataline16, instructionLine, AddSub.add(instructionPointer, 4), offsetLine};
        U115.loadArguments(input);
        instructionPointer = U115.calculate()[0] & 0xFFFF;

        // Only if writing to memory
        input = new int[] {dataline8, dataline16, ALU, dst};
        U220.loadArguments(input);
        U220.calculate();


        // write back to register
        writeRegisters();
        writeMemory();
    }


    /**
     *
     */
    private void writeMemory() {
        if (U220.canWrite()) {
            try {
                memory.write(U116.transmit()[0], U220.transmit()[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Writes to registers if the enable line was asserted
     * for the specific line
     */
    private void writeRegisters() {
        switch (dst) {
            case 0:
                if (U114A.isEnabled())
                    U10.setData(U118A.transmit()[0]);
                if (U114B.isEnabled())
                    U10.setData(U118B.transmit()[0]);
                break;
            case 1:
                if (U114A.isEnabled())
                    U11.setData(U118A.transmit()[0]);
                if (U114B.isEnabled())
                    U11.setData(U118B.transmit()[0]);
                break;
            case 2:
                if (U114A.isEnabled())
                    U12.setData(U118A.transmit()[0]);
                if (U114B.isEnabled())
                    U12.setData(U118B.transmit()[0]);
                break;
            case 3:
                if (U114A.isEnabled())
                    U13.setData(U118A.transmit()[0]);
                if (U114B.isEnabled())
                    U13.setData(U118B.transmit()[0]);
                break;
        }
    }


    /**
     * Reads only if the write line was asserted low
     *
     * if low then returns the read at the specific memory location
     *
     * @return data specified memory location
     */
    private int readMemory() {
        int memRead = 0;
        // Only write or read mode not both
        if (U220.canRead()) {
            try {
                // Read memory address specified by U116
                memRead = memory.fetch(U116.transmit()[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return memRead;
    }


    private void checkCondition(int modifier) {
        U115.setInputSelector(convertToBinaryArray(2));
        switch (modifier & 0xf) {
            case 0x6:
                if ((flags & 0b1) == 1)
                    U115.setInputSelector(convertToBinaryArray(3));
                break;
            case 0x7:
                if ((flags & 0b1) == 0)
                    U115.setInputSelector(convertToBinaryArray(3));
                break;
            case 0x8:
                if ((flags >>> 3 & 0b1) == 1)
                    U115.setInputSelector(convertToBinaryArray(3));
                break;
            case 0x9:
                if ((flags >>> 3 & 0b1) == 0)
                    U115.setInputSelector(convertToBinaryArray(3));
                break;
            case 0xa:
                if ((flags >>> 1 & 0b1) == 1)
                    U115.setInputSelector(convertToBinaryArray(3));
                break;
            case 0xb:
                if ((flags >>> 1 & 0b1) == 0)
                    U115.setInputSelector(convertToBinaryArray(3));
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
        return "Reg0(U10): 0x" + Integer.toHexString(U10.getData()).toUpperCase() + "\n" +
                "Reg1(U11): 0x" + Integer.toHexString(U11.getData()).toUpperCase() + "\n" +
                "Reg2(U12): 0x" + Integer.toHexString(U12.getData()).toUpperCase() + "\n" +
                "Reg3(U13): 0x" + Integer.toHexString(U13.getData()).toUpperCase() + "\n";
    }


    /**
     *
     * @return
     */
    public String dumpFlags() {
        return "Z: " + (flags >> 3 & 0b1) + "\n" +
               "V: " + (flags >> 2 & 0b1) + "\n" +
               "N: " + (flags >> 1 & 0b1) + "\n" +
               "C: " + (flags & 0b1) + "\n";
    }


    /**
     *
     * @param number
     * @return
     */
    private int[] convertToBinaryArray(int number) {
        int arrayLength = (int) Math.floor(Math.log(number) / Math.log(2)) + 1;
        if (number == 0)
            arrayLength = 1;
        int[] convertArray = new int[arrayLength];
        for (int i = 0; number != 0; i++) {
            convertArray[i] = number % 2;
            number >>>= 1;
        }
        reverse(convertArray);
        return convertArray;
    }


    /**
     *
     * @param data
     */
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
