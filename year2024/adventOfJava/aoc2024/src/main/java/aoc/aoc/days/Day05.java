package aoc.aoc.days;

import aoc.aoc.util.*;
import lombok.experimental.Accessors;

import java.util.*;

public class Day05 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        var pair = getRulesAndUpdates(input);
        List<String> rules = pair.first();
        List<String> updates = pair.second();

        Map<String, Set<String>> priorities = getPriorities(rules);
        updates = filterUpdatesByPriority(updates, priorities);

        return "" + sumMiddleNumbers(updates);
    }

    @Override
    protected String part2Impl(String input) {
        var pair = getRulesAndUpdates(input);
        List<String> rules = pair.first();
        List<String> updates = pair.second();

        Map<String, Set<String>> priorities = getPriorities(rules);
        var alreadyInOrder = filterUpdatesByPriority(updates, priorities);

        List<String> reOrderedUpdates = new ArrayList<>();
        for (String update : updates) {
            if (!alreadyInOrder.contains(update)) {
                reOrderedUpdates.add(reorderUpdate(update, priorities));
            }
        }

        return "" + sumMiddleNumbers(reOrderedUpdates);
    }

    private static Pair<List<String>, List<String>> getRulesAndUpdates(String input) {
        String[] split = input.split("(\\R){2}");
        return new Pair<>(
                split[0].lines().toList(),
                split[1].lines().toList()
        );
    }

    private static Map<String, Set<String>> getPriorities(List<String> rules) {
        Map<String, Set<String>> priorities = new HashMap<>();
        for (var rule : rules) {
            String[] nums = rule.split("\\|");
            String left = nums[0];
            String right = nums[1];

            priorities.compute(left, (k, v) -> {
                v = v != null ? v : new HashSet<>();
                v.add(right);
                return v;
            });
        }
        return priorities;
    }

    private static List<String> filterUpdatesByPriority(List<String> updates, Map<String, Set<String>> priorities) {
        updates = updates.stream().filter(update -> {
            String[] numbers = update.split(",");
            return canUpdate(numbers, priorities);
        }).toList();
        return updates;
    }

    private static boolean canUpdate(String[] numbers, Map<String, Set<String>> priorities) {
        for (int number = 0; number < numbers.length; number++) {
            for (int rightOf = number + 1; rightOf < numbers.length; rightOf++) {
                var priority = priorities.get(numbers[rightOf]);
                if (priority != null && priority.contains(numbers[number]))
                    return false;
            }
        }

        return true;
    }

    private static int sumMiddleNumbers(List<String> updates) {
        return updates.stream()
                .mapToInt(a -> {
                    var numbers = a.split(",");
                    return Integer.parseInt(numbers[numbers.length / 2]);
                }).sum();
    }

    private static String reorderUpdate(String update, Map<String, Set<String>> priorities) {
        var numbers = update.split((","));

        var bst = new TreeSet<PriorityNode>();
        for (String number : numbers) {
            bst.add(new PriorityNode(number, priorities.get(number)));
        }

        var sb = new StringBuilder();
        bst.forEach(n -> sb.append("%s,".formatted(n.value())));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Accessors(fluent = true)
    private record PriorityNode(String value, Set<String> priority) implements Comparable<PriorityNode> {

        @SuppressWarnings("java:S1210")
        @Override
        public int compareTo(PriorityNode o) {
            if (o.priority() != null && o.priority().contains(value)) {
                return 1;
            } else if (priority != null && priority.contains(o.value())) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
