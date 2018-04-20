package com.arch.Emulator;

public class Memory {

    private char[][] ram;
    private int dataLocation = 0;
    private int programLocation = 0;
    private int endOfProgram = 0;

    /**
     * Main memory for the cpu architecture with a 16-bit range of memory to
     * use
     */
    Memory() {
        // create memory for range of 0x0000 0xFFFF
        ram = new char[16][];
        for (int i = 0; i < 16; i++) {
            ram[i] = new char[0x1000];
        }
    }

    /**
     * Writes an opcode to a the end of the program location for reading during execution.
     * <p>
     *     The start of each program will start at address of EPROM (0x4000). The program location will always start writing
     *     to this section of memory. And for every opcode received it will write the opcode to the last location.
     *     Each opcode is read as 4 bytes and each byte is written one at a time to a specific offset. The opcodes
     *     are written always in 4 bytes and in little endian memory.
     * </p>
     * <p>
     *     Each of the upper byte of the program location will determine the writing location of what chip to write to.
     *     The lower 16 bits will determine what offset in the chip specifically to write to.
     * </p>
     * <p>
     *     <br>Example: 0x4302 address to write opcode 8094.<br>
     *
     *     <br>ram chip = 4 // where 4 is the ram chip in memory<br>
     *     chipOffset = 0x302 // where the lower 16 bits are use for the offset in the chip<br>
     *
     *     <br> So we write to location ram[4][0x302] = 00<br>
     *     So we write to location ram[4][0x303] = 00<br>
     *     So we write to location ram[4][0x304] = 80<br>
     *     So we write to location ram[4][0x305] = 94
     * </p>
     * @param opcode the instruction to write to memory
     */
    public void loadInstruction(int opcode) {
        // Check if the program has been loaded yet into EPROM
        if (programLocation == 0)
            programLocation = 0xF000;

        int ramChip = (programLocation & 0xFFFF) >> 12; // upper byte as ram chip selection
        int ramChipOffset = (programLocation & 0xFFF); // lower 16 bits as chip offset

        // Write the lower bits of the opcode in little endian
        for (int i = 0; i < 4; i++, opcode >>= 8)
            ram[ramChip][ramChipOffset + i] = (char) (opcode & 0xFF);

        programLocation += 4;
        endOfProgram = programLocation; // mark end of written program
    }

    public int getEndOfProgram() {
        return endOfProgram;
    }


    /**
     * Reads a fetch on a memory location anywhere below 0xF000
     *
     * <p>
     *     Throws an IllegalAccessException at an illegal memory request.
     *     This maybe anywhere at a EPROM location (0xF000) or and out of bounds location
     * </p>
     *
     * @param memoryFetch memory location to read at
     * @return data at requested memory fetch
     */
    public int fetch(int memoryFetch) throws IllegalAccessException {
        if (memoryFetch >= 0xF000) {
            if (memoryFetch <= 0xFFFF)
                throw new IllegalAccessException("Illegal access of memory to protected memory at 0x"
                        + Integer.toHexString(memoryFetch));
            else
                throw new IllegalAccessException("Illegal access of out of bound memory at 0x"
                        + Integer.toHexString(memoryFetch));
        }
        int ramChip = (memoryFetch & 0xFFFF) >> 12; // Use upper byte as ram chip selection
        int ramChipOffset = (memoryFetch & 0xFFF); // Use lower 16 bits as chip offset
        return ram[ramChip][ramChipOffset];
    }

    public int secureFetch(int memoryFetch) throws IllegalAccessException {
        if (memoryFetch > 0xFFFF)
            throw new IllegalAccessException("Illegal access of out of bound memory at 0x"
                    + Integer.toHexString(memoryFetch));
        int ramChip = (memoryFetch & 0xFFFF) >> 12; // Use upper byte as ram chip selection
        int ramChipOffset = (memoryFetch & 0xFFF); // Use lower 16 bits as chip offset
        return ram[ramChip][ramChipOffset];
    }

    public void write(int memoryFetch, int data) throws IllegalAccessException {
        if (memoryFetch >= 0xF000) {
            if (memoryFetch <= 0xFFFF)
                throw new IllegalAccessException("Illegal access of memory to protected memory at 0x"
                        + Integer.toHexString(memoryFetch));
            else
                throw new IllegalAccessException("Illegal access of out of bound memory at 0x"
                        + Integer.toHexString(memoryFetch));
        }
        int ramChip = (memoryFetch & 0xFFFF) >> 12; // Use upper byte as ram chip selection
        int ramChipOffset = (memoryFetch & 0xFFF); // Use lower 16 bits as chip offset
        ram[ramChip][ramChipOffset] = (char) data;
    }

    /**
     * Reads every location in ram and specifies what section of ram is being read and the contents of the chip
     *
     * @return full string contents of the ram
     */
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
                sb.append(String.format(" %2s", Integer.toHexString(ram[i][j])).toUpperCase());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
