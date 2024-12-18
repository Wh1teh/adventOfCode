package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Consumer;

public class Day17 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var result = new Computer(input)
                .runProgram()
                .output();
        return toStringStripBracketsAndWhiteSpace(result);
    }

    @Override
    protected String part2Impl(String input) {
        var computer = new Computer(input).with(Part.PART_2);
        var originalProgram = computer.program();
        var program = new ArrayList<>(computer.program());

        long registerA = 0;
        while (!program.isEmpty() && program.removeLast() != null) {
            registerA = Math.max(0, (registerA << 3) - 1);
            do {
                computer.resetWith(++registerA, program)
                        .runProgram();
            } while (!computer.output().equals(originalProgram));
        }

        return "" + registerA;
    }

    private static String toStringStripBracketsAndWhiteSpace(List<?> list) {
        return list.toString()
                .replaceAll("[\\[\\] ]", "");
    }

    @Accessors(fluent = true)
    private static class Computer extends AbstractSolver<Computer> {

        @Data
        @Accessors(fluent = true)
        @AllArgsConstructor
        private static class Register {
            long a;
            long b;
            long c;

            long get(short operand) {
                return switch (operand) {
                    case 4 -> a;
                    case 5 -> b;
                    case 6 -> c;
                    default -> throw new UnsupportedOperationException("No such index in register");
                };
            }
        }

        private final List<Consumer<Short>> instructions = List.of(
                this::adv, this::bxl, this::bst, this::jnz,
                this::bxc, this::out, this::bdv, this::cdv
        );

        private final Register register;
        private final List<Short> program;

        private int instructionPointer = 0;

        final List<Short> output = new ArrayList<>();
        boolean shouldHalt = false;

        public Computer(String input) {
            var parts = input.split("\\R{2}");
            this.register = parseRegister(parts[0]);
            this.program = parseProgram(parts[1]);
        }

        public Computer runProgram() {
            while (instructionPointer < program.size() && !shouldHalt) {
                runInstruction(program.get(instructionPointer), program.get(instructionPointer + 1));
                instructionPointer += 2;
            }

            return this;
        }

        public List<Short> output() {
            return output;
        }

        public List<Short> program() {
            return program;
        }

        public Computer resetWith(long a, List<Short> forceCurrentOutput) {
            instructionPointer = 0;
            register.a(a).b(0).c(0);
            output.clear();
            output.addAll(forceCurrentOutput);
            shouldHalt = false;

            return this;
        }

        private void runInstruction(short opcode, short operand) {
            if (opcode > 7)
                throw new UnsupportedOperationException("No opcode for: %d".formatted(opcode));

            instructions.get(opcode).accept(operand);
        }

        private long getByComboOperand(short operand) {
            return operand <= 3 ? operand : register.get(operand);
        }

        private void adv(short operand) {
            long result = performDivision(operand);
            register.a(result);
        }

        private void bxl(short operand) {
            long result = register.b() ^ operand;
            register.b(result);
        }

        private void bst(short operand) {
            long result = firstThreeBitsOfCombo(operand);
            register.b(result);
        }

        private void jnz(short operand) {
            if (register.a() == 0)
                return;

            instructionPointer = operand - 2;
        }

        private void bxc(short operand) {
            long result = register.b() ^ register.c();
            register.b(result);
        }

        private void out(short operand) {
            long result = firstThreeBitsOfCombo(operand);
            if (part == Part.PART_2 && outputMismatch((short) result))
                shouldHalt = true;
            output.add((short) result);
        }

        private void bdv(short operand) {
            long result = performDivision(operand);
            register.b(result);
        }

        private void cdv(short operand) {
            long result = performDivision(operand);
            register.c(result);
        }

        private long firstThreeBitsOfCombo(short operand) {
            return getByComboOperand(operand) & 0b111;
        }

        /**
         * These are the same:
         * <pre>
         *     {@code return (long) (register.a() / Math.pow(2, getByComboOperand(operand)));}
         *     {@code return register.a() / (1L << getByComboOperand(operand));}
         *     {@code return register.a() >> getByComboOperand(operand);}
         * </pre>
         */
        private long performDivision(short operand) {
            return register.a() >> getByComboOperand(operand);
        }

        private boolean outputMismatch(short result) {
            return output.size() < program.size() && result != program.get(output.size());
        }

        private static Register parseRegister(String input) {
            var entries = input.lines()
                    .map(line -> Long.parseLong(parsePastColon(line)))
                    .toList();
            return new Register(entries.get(0), entries.get(1), entries.get(2));
        }

        private static List<Short> parseProgram(String input) {
            return Arrays.stream(parsePastColon(input).split(","))
                    .map(Short::parseShort)
                    .toList();
        }

        private static String parsePastColon(String line) {
            return line.split(":")[1].strip();
        }
    }
}
