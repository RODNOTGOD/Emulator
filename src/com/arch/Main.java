package com.arch;

import com.arch.Emulator.Enumlator;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {

        Enumlator enumlator = new Enumlator();

        try {
            String line;
            FileReader file = new FileReader("input.txt");
            BufferedReader reader = new BufferedReader(file);
            while ((line = reader.readLine()) != null) {

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean isComment(String line) {
        return !(line != null && line.matches("[-+]?\\d*\\.?\\d+"));
    }
}
