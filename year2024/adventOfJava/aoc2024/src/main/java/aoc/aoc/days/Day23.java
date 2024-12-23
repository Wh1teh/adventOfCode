package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Utils;

import java.util.*;
import java.util.function.Predicate;

public class Day23 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var a = new PartyNetwork(input)
                .lookForNetworksContainingComputerTypeT();
        return "" + a;
    }

    @Override
    protected String part2Impl(String input) {
        var largestConnection = new PartyNetwork(input)
                .largestConnection();
        return Utils.toStringStripBracketsAndWhiteSpace(largestConnection);
    }

    private static class PartyNetwork extends AbstractSolver<PartyNetwork> {

        public static final String COMPUTER_TYPE_TO_LOOK_FOR = "t";
        private final Map<String, Set<String>> network;

        public PartyNetwork(String input) {
            this.network = createGraphFromConnections(input);
        }

        public Set<String> largestConnection() {
            Set<String> largestInterconnection = Collections.emptySet();

            for (var computer : network.entrySet()) {
                var filtered = filterNonInterconnected(computer.getKey(), computer.getValue());
                if (filtered.size() > largestInterconnection.size())
                    largestInterconnection = filtered;
            }

            return largestInterconnection;
        }

        private Set<String> filterNonInterconnected(String computer, Set<String> connections) {
            Set<String> copy = new TreeSet<>(connections);
            copy.add(computer);

            connections.stream().filter(copy::contains).forEach(connected -> {
                var connectsTo = network.get(connected);
                connectsTo.add(connected);
                copy.retainAll(connectsTo);
            });

            return copy;
        }

        public int lookForNetworksContainingComputerTypeT() {
            return networksContainingComputer(s -> s.startsWith(COMPUTER_TYPE_TO_LOOK_FOR));
        }

        private int networksContainingComputer(Predicate<String> predicate) {
            Set<Set<String>> matches = new HashSet<>();

            network.forEach((computer, connections) -> {
                if (!predicate.test(computer))
                    return;

                connections.forEach(c1 ->
                        network.get(c1).forEach(c2 -> {
                            if (network.get(c2).contains(computer)) {
                                matches.add(Set.of(computer, c1, c2));
                            }
                        })
                );
            });

            return matches.size();
        }

        @SuppressWarnings("java:S117")
        private Map<String, Set<String>> createGraphFromConnections(String input) {
            Map<String, Set<String>> connections = new HashMap<>();

            input.lines().forEach(line -> {
                var computers = line.split("-");
                connections.computeIfAbsent(computers[0], __ -> new HashSet<>()).add(computers[1]);
                connections.computeIfAbsent(computers[1], __ -> new HashSet<>()).add(computers[0]);
            });

            return connections;
        }
    }
}
