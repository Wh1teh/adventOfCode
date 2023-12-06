package com.aoc.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenerateDays {

    public static void main(String[] args) {
        generate("year2023/adventOfJava/src/main/java/com/aoc/", "com.aoc.", 7, 25);
    }

    public static void generate(String path, String packageBase, int from, int to) {
        String[] folders = path.split("/");

        if (!checkRootFolder(folders[0])) {
            throw new IllegalStateException("no %s in root folder".formatted(folders[0]));
        }

        for (int number = from; number <= to; number++) {
            String dayNum = String.format("%02d", number);
            String fileName = path + "days/Day" + dayNum + ".java";

            String content = "package com.aoc.days;\n" + //
                    "\n" + //
                    "public class Day" + dayNum + " extends Day {\n" + //
                    "\n" + //
                    "    @Override\n" + //
                    "    public String solveFirstPart() {\n" + //
                    "\n" + //
                    "        return \"\";\n" + //
                    "    }\n" + //
                    "\n" + //
                    "    @Override\n" + //
                    "    public String solveSecondPart() {\n" + //
                    "\n" + //
                    "        return \"\";\n" + //
                    "    }\n" + //
                    "\n" + //
                    "}";

            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(content);
                System.out.println("File created: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkRootFolder(String yearFolder) {
        try {
            // Command to list files in the current directory (ls on Unix-like systems)
            // For Windows, you can use "cmd.exe", "/c", "dir" instead
            String[] command = { "ls" };

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            process.waitFor();

            // Print the output
            System.out.println("Command Output:\n" + output.toString());

            return output.toString().contains(yearFolder);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
