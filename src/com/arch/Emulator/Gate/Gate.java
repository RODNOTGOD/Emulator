package com.arch.Emulator.Gate;

import java.util.Vector;

public interface Gate {
    int transmit();
    void loadArguments(int[] inputs);
    int calculate();
}
