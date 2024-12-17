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
        return new Computer(input)
                .runProgram()
                .output();
    }

    @Override
    protected String part2Impl(String input) {
        var computer = new Computer(input).with(Part.PART_2);
        var programAsString = computer.program();

        if (true)
            throw new UnsupportedOperationException("not working");

        long aRegister = 0;
        while (!computer.output().equals(programAsString)) {
            if (aRegister % 1_000_000 == 0)
                System.out.println("Trying with " + aRegister);

            computer.resetWith(aRegister++)
                    .runProgram();
        }
        return "" + (aRegister - 1);
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
        private boolean pointerHasJumped = false;

        final List<Short> output = new ArrayList<>();

        public Computer(String input) {
            var parts = input.split("\\R{2}");
            this.register = parseRegister(parts[0]);
            this.program = parseProgram(parts[1]);
        }

        public Computer runProgram() {
            while (instructionPointer < program.size()) {
                runInstruction(program.get(instructionPointer), program.get(instructionPointer + 1));
                if (pointerHasJumped)
                    pointerHasJumped = false;
                else
                    instructionPointer += 2;
            }

            return this;
        }

        public String output() {
            return toStringStripBracketsAndWhiteSpace(output);
        }

        public String program() {
            return toStringStripBracketsAndWhiteSpace(program);
        }

        private static String toStringStripBracketsAndWhiteSpace(List<?> list) {
            return list.toString()
                    .replaceAll("[\\[\\] ]", "");
        }

        public Computer resetWith(long a) {
            instructionPointer = 0;
            register.a(a).b(0).c(0);
            output.clear();
            pointerHasJumped = false;

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
            long result = (long) (register.a() / Math.pow(2, getByComboOperand(operand)));
            register.a(result);
        }

        private void bxl(short operand) {
            long result = register.b() ^ operand;
            register.b(result);
        }

        private void bst(short operand) {
            long result = getByComboOperand(operand) % 8;
            register.b(result);
        }

        private void jnz(short operand) {
            if (register.a() == 0 || instructionPointer == operand)
                return;

            pointerHasJumped = true;
            instructionPointer = operand;
        }

        private void bxc(short operand) {
            long result = register.b() ^ register.c();
            register.b(result);
        }

        private void out(short operand) {
            long result = getByComboOperand(operand) % 8;
            output.add((short) result);
        }

        private void bdv(short operand) {
            long result = (long) (register.a() / Math.pow(2, getByComboOperand(operand)));
            register.b(result);
        }

        private void cdv(short operand) {
            long result = (long) (register.a() / Math.pow(2, getByComboOperand(operand)));
            register.c(result);
        }

        private static Register parseRegister(String input) {
            var entries = input.lines()
                    .map(line -> Long.parseLong(getPastColon(line)))
                    .toList();
            assert entries.size() == 3;
            return new Register(entries.get(0), entries.get(1), entries.get(2));
        }

        private static List<Short> parseProgram(String input) {
            return Arrays.stream(getPastColon(input).split(","))
                    .map(Short::parseShort)
                    .toList();
        }

        private static String getPastColon(String line) {
            return line.split(":")[1].strip();
        }
    }
}
