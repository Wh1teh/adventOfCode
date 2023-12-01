package com.aoc.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenerateDays {

    public static void generate(String path, String packageBase) {
        String[] folders = path.split("/");

        if (!checkRootFolder(folders[0])) {
            throw new IllegalStateException("no %s in root folder".formatted(folders[0]));
        }

        for (int number = 0; number <= 25; number++) {
            String dayNum = String.format("%02d", number);
            String fileName = path + "days/Day" + dayNum + ".java";

            String content = "package " + packageBase + "days;\n" + //
                    "\n" + //
                    "import " + packageBase + "util.ReadFile;\n" + //
                    "\n" + //
                    "import java.util.List;\n" + //
                    "\n" + //
                    "public class Day" + dayNum + " implements DayInterface {\n" + //
                    "\n" + //
                    "    public String solve(int part) {\n" + //
                    "        List<String> lines = ReadFile.getData(\n" + //
                    "                \"" + path + "data/day" + dayNum
                    + "\" + (part == 0 ? \"_sample\" : \"\") + \".txt\");\n"
                    + //
                    "        if (lines == null || lines.size() == 0) {\n" + //
                    "            return \"Day " + dayNum + " is not available\";\n" + //
                    "        }\n" + //
                    "        StringBuilder str = new StringBuilder();\n" + //
                    "\n" + //
                    "        str = switch (part) {\n" + //
                    "            case 0 -> solveSample(lines);\n" + //
                    "            case 1 -> solveFirstPart(lines);\n" + //
                    "            case 2 -> solveSecondPart(lines);\n" + //
                    "            default -> throw new IllegalArgumentException(\"Unexpected value: \" + part);\n" + //
                    "        };\n" + //
                    "\n" + //
                    "        if (str.isEmpty())\n" + //
                    "            return \"Day " + dayNum + " part \" + part + \" is Unimplemented\";\n" + //
                    "        return str.toString();\n" + //
                    "    }\n" + //
                    "\n" + //
                    "    public StringBuilder solveSample(List<String> lines) {\n" + //
                    "        StringBuilder result = new StringBuilder();\n" + //
                    "\n" + //
                    "        result.append(solveFirstPart(lines));\n" + //
                    "        result.append(\"\\n" + //
                    "---\\n" + //
                    "\");\n" + //
                    "        result.append(solveSecondPart(lines));\n" + //
                    "\n" + //
                    "        return result;\n" + //
                    "    }\n" + //
                    "\n" + //
                    "    public StringBuilder solveFirstPart(List<String> lines) {\n" + //
                    "        StringBuilder result = new StringBuilder();\n" + //
                    "\n" + //
                    "        return result;\n" + //
                    "    }\n" + //
                    "\n" + //
                    "    public StringBuilder solveSecondPart(List<String> lines) {\n" + //
                    "        StringBuilder result = new StringBuilder();\n" + //
                    "\n" + //
                    "        return result;\n" + //
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
