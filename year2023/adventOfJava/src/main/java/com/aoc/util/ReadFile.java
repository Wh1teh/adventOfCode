package com.aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadFile {
    public static List<String> getData(String path) {
        return getData(path, false);
    }
    
    public static List<String> getData(String path, boolean debug) {
        List<String> lines = null;

        try {
            // read the file into a string
            lines = Files.readAllLines(Paths.get(path));
            if (debug) {
                System.out.println("File Content:\n" + lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
