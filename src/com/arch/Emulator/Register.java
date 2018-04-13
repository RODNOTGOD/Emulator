package com.arch.Emulator;

import java.util.Random;

public class Register {

    String id;
    int data;

    public Register() {
        Random random = new Random(0xFFF);
        data = random.nextInt();
    }

    public Register(String id) {
        Random random = new Random();
        data = random.nextInt(0xFFFF);
        this.id = id;
        System.out.println("REGISTER " + this.id + ": inialitzed with 0x" + Integer.toHexString(data));
    }


}
