package com.aoc.days;

import com.aoc.util.ReadFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day02 implements DayInterface {

    public String solve(int part) {
        List<String> lines = ReadFile.getData(
                "src/main/java/com/aoc/data/day02" + (part == 0 ? "_sample" : "") + ".txt");
        if (lines == null || lines.size() == 0) {
            return "Day 02 is not available";
        }
        StringBuilder str = new StringBuilder();

        str = switch (part) {
            case 0 -> solveSample(lines);
            case 1 -> solveFirstPart(lines);
            case 2 -> solveSecondPart(lines);
            default -> throw new IllegalArgumentException("Unexpected value: " + part);
        };

        if (str.isEmpty())
            return "Day 02 part " + part + " is Unimplemented";
        return str.toString();
    }

    public StringBuilder solveSample(List<String> lines) {
        StringBuilder result = new StringBuilder();

        result.append(solveFirstPart(lines));
        result.append("\n---\n");
        result.append(solveSecondPart(lines));

        return result;
    }

    /*
     * --- Day 2: Cube Conundrum ---
     * You're launched high into the atmosphere! The apex of your trajectory just
     * barely reaches the surface of a large island floating in the sky. You gently
     * land in a fluffy pile of leaves. It's quite cold, but you don't see much
     * snow. An Elf runs over to greet you.
     * 
     * The Elf explains that you've arrived at Snow Island and apologizes for the
     * lack of snow. He'll be happy to explain the situation, but it's a bit of a
     * walk, so you have some time. They don't get many visitors up here; would you
     * like to play a game in the meantime?
     * 
     * As you walk, the Elf shows you a small bag and some cubes which are either
     * red, green, or blue. Each time you play this game, he will hide a secret
     * number of cubes of each color in the bag, and your goal is to figure out
     * information about the number of cubes.
     * 
     * To get information, once a bag has been loaded with cubes, the Elf will reach
     * into the bag, grab a handful of random cubes, show them to you, and then put
     * them back in the bag. He'll do this a few times per game.
     * 
     * You play several games and record the information from each game (your puzzle
     * input). Each game is listed with its ID number (like the 11 in Game 11: ...)
     * followed by a semicolon-separated list of subsets of cubes that were revealed
     * from the bag (like 3 red, 5 green, 4 blue).
     * 
     * For example, the record of a few games might look like this:
     * 
     * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
     * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
     * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
     * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
     * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
     * In game 1, three sets of cubes are revealed from the bag (and then put back
     * again). The first set is 3 blue cubes and 4 red cubes; the second set is 1
     * red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green
     * cubes.
     * 
     * The Elf would first like to know which games would have been possible if the
     * bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
     * 
     * In the example above, games 1, 2, and 5 would have been possible if the bag
     * had been loaded with that configuration. However, game 3 would have been
     * impossible because at one point the Elf showed you 20 red cubes at once;
     * similarly, game 4 would also have been impossible because the Elf showed you
     * 15 blue cubes at once. If you add up the IDs of the games that would have
     * been possible, you get 8.
     * 
     * Determine which games would have been possible if the bag had been loaded
     * with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of
     * the IDs of those games?
     */
    public StringBuilder solveFirstPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        Map<String, Integer> dice = new HashMap<>();
        dice.put("red", 12);
        dice.put("green", 13);
        dice.put("blue", 14);

        int sum = 0;
        for (String line : lines) {
            sum += playGame(line, dice, 1);
        }

        return result.append(sum);
    }

    private String parseColor(String line, int[] index) {
        StringBuilder color = new StringBuilder();
        while (index[0] < line.length() && Character.isLetter(line.charAt(index[0]))) {
            color.append(line.charAt(index[0]++));
        }
        return color.toString();
    }

    private String parseAmount(String line, int[] index) {
        StringBuilder number = new StringBuilder();
        while (index[0] < line.length() && Character.isDigit(line.charAt(index[0]))) {
            number.append(line.charAt(index[0]++));
        }
        return number.toString();
    }

    private int playGame(String line, Map<String, Integer> diceBase, int part) {
        int[] index = { 0 }; // "array" so it stays mutable when passed to methods

        // go to start of the game
        while (index[0] < line.length() && !Character.isWhitespace(line.charAt(index[0]++))) {
        }
        String game = parseAmount(line, index);
        index[0] += 2; // hop to first game

        // play through the games
        Map<String, Integer> dice = new HashMap<>();
        Map<String, Integer> minDice = new HashMap<>();
        minDice.putAll(diceBase);
        dice.putAll(diceBase);
        while (index[0] < line.length()) {
            switch (line.charAt(index[0])) {
                case ';' -> {
                    dice.putAll(diceBase);
                    index[0] += 2;
                }
                case ',' -> index[0] += 2;
            }
            ;

            // get amount of drawn dice
            String drawAmount = parseAmount(line, index);
            index[0]++; // hop to identifier

            // decrease from color
            String color = parseColor(line, index);
            int diceLeft = dice.get(color) - Integer.parseInt(drawAmount);
            dice.put(color, diceLeft);

            // part 2 stuff
            if (diceLeft < minDice.get(color)) {
                minDice.put(color, diceLeft);
            }

            if (diceLeft < 0) {
                // game is impossible
                game = "0";
                // break;
            }
        }

        if (part == 2) {
            int power = 1;

            System.out.println("game:" + game);
            // get power for part 2
            for (Map.Entry<String, Integer> entry : minDice.entrySet()) {
                String key = entry.getKey();
                Integer diceLeft = entry.getValue();

                System.out.println(key + ":" + (diceBase.get(key) - diceLeft));

                int minimumDice = diceBase.get(key) - diceLeft;
                power *= minimumDice > 0 ? minimumDice : 1;
            }

            System.out.println("returning power: " + power);
            return power;
        }

        return Integer.parseInt(game);
    }

    /*
     * --- Part Two ---
     * The Elf says they've stopped producing snow because they aren't getting any
     * water! He isn't sure why the water stopped; however, he can show you how to
     * get to the water source to check it out for yourself. It's just up ahead!
     * 
     * As you continue your walk, the Elf poses a second question: in each game you
     * played, what is the fewest number of cubes of each color that could have been
     * in the bag to make the game possible?
     * 
     * Again consider the example games from earlier:
     * 
     * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
     * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
     * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
     * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
     * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
     * In game 1, the game could have been played with as few as 4 red, 2 green, and
     * 6 blue cubes. If any color had even one fewer cube, the game would have been
     * impossible.
     * Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue
     * cubes.
     * Game 3 must have been played with at least 20 red, 13 green, and 6 blue
     * cubes.
     * Game 4 required at least 14 red, 3 green, and 15 blue cubes.
     * Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag.
     * The power of a set of cubes is equal to the numbers of red, green, and blue
     * cubes multiplied together. The power of the minimum set of cubes in game 1 is
     * 48. In games 2-5 it was 12, 1560, 630, and 36, respectively. Adding up these
     * five powers produces the sum 2286.
     * 
     * For each game, find the minimum set of cubes that must have been present.
     * What is the sum of the power of these sets?
     */
    public StringBuilder solveSecondPart(List<String> lines) {
        StringBuilder result = new StringBuilder();

        Map<String, Integer> dice = new HashMap<>();
        dice.put("red", 12);
        dice.put("green", 13);
        dice.put("blue", 14);

        int sum = 0;
        for (String line : lines) {
            sum += playGame(line, dice, 2);
        }

        return result.append(sum);
    }

}