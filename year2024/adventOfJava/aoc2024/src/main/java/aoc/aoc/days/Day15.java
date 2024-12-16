package aoc.aoc.days;

import aoc.aoc.solver.AbstractSolver;
import aoc.aoc.util.Coordinate;
import aoc.aoc.util.Direction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc.aoc.days.Part.PART_1;
import static aoc.aoc.days.Part.PART_2;

public class Day15 extends AbstractDay {

    @Override
    protected String part1Impl(String input) {
        return "" + new Sokoban2(input, PART_1)
                .executeAllMoves()
                .countGpsCoordinates();
    }

    @Override
    protected String part2Impl(String input) {
        return "" + new Sokoban2(input, PART_2)
                .executeAllMoves()
                .countGpsCoordinates();
    }

    private static class Sokoban2 extends AbstractSolver<Sokoban2> {

        private final Map<Coordinate, Entity> positions;
        private final Deque<Direction> directions;
        private Entity robot;

        public Sokoban2(String input, Part part) {
            this.part = part;

            var parts = input.split("\\R{2}");
            this.positions = parseEntities(parts[0]);
            this.directions = parseDirections(parts[1]);
        }

        public Sokoban2 executeAllMoves() {
            for (var direction : directions) {
                moveRobot(direction);
            }

            return this;
        }

        public int countGpsCoordinates() {
            var sum = new AtomicInteger(0);
            var counted = new HashSet<Entity>();
            positions.forEach((coordinate, entity) -> {
                if (entity.type() != Entity.Type.BOX || !counted.add(entity))
                    return;

                var left = entity.positions().getFirst();
                sum.set(sum.get() + (left.y() * 100 + left.x()));
            });

            return sum.get();
        }

        private void moveRobot(Direction direction) {
            if (canNotMove(robot, robot.positions().getFirst(), direction))
                return;

            var moved = new HashSet<Entity>();
            moveEntity(robot, direction, moved);

            moved.forEach(entity -> entity.moved(false));
        }

        private void moveEntity(Entity entity, Direction direction, Set<Entity> moved) {
            if (entity == null || entity.type() == Entity.Type.WALL || entity.moved())
                return;
            moved.add(entity);
            entity.moved(true);

            int index = 0;
            for (var position : entity.positions()) {
                var nextPosition = nextPosition(position, direction);
                var nextEntity = positions.get(nextPosition);

                moveEntity(nextEntity, direction, moved);

                entity.positions().set(index, nextPosition);

                if (direction != Direction.RIGHT || index != 1)
                    positions.remove(position);
                positions.put(nextPosition, entity);

                index++;
            }
        }

        private boolean canNotMove(Entity entity, Coordinate from, Direction direction) {
            var nextPosition = nextPosition(from, direction);
            var nextEntity = positions.get(nextPosition);
            if (nextEntity == null)
                return false;
            else if (nextEntity.equals(entity))
                return false;
            else if (nextEntity.type() == Entity.Type.WALL)
                return true;
            else {
                for (var pos : nextEntity.positions()) {
                    if (canNotMove(nextEntity, pos, direction))
                        return true;
                }
                return false;
            }
        }

        private Coordinate nextPosition(Coordinate position, Direction direction) {
            int y = position.y();
            int x = position.x();
            return switch (direction) {
                case UP -> new Coordinate(y - 1, x);
                case RIGHT -> new Coordinate(y, x + 1);
                case DOWN -> new Coordinate(y + 1, x);
                case LEFT -> new Coordinate(y, x - 1);
            };
        }

        private Map<Coordinate, Entity> parseEntities(String input) {
            var parsedPositions = new HashMap<Coordinate, Entity>();

            var lines = input.lines()
                    .map(line -> line.chars()
                            .mapToObj(c -> (char) c).toList()
                    ).toList();

            for (int i = 0; i < lines.size(); i++) {
                var line = lines.get(i);
                for (int j = 0; j < line.size(); j++) {
                    char ch = line.get(j);
                    if (part == PART_1)
                        initEntityPart1(ch, i, j, parsedPositions);
                    else
                        initEntityPart2(ch, i, j, parsedPositions);
                }
            }

            return parsedPositions;
        }

        private void initEntityPart1(char ch, int i, int j, Map<Coordinate, Entity> parsedPositions) {
            if (Entity.Type.fromChar(ch) == Entity.Type.EMPTY)
                return;

            var position = new Coordinate(i, j);
            var entity = new Entity(new ArrayList<>(List.of(position)), ch);
            parsedPositions.put(position, entity);

            if (entity.type() == Entity.Type.ROBOT)
                this.robot = entity;
        }

        private void initEntityPart2(char ch, int i, int j, Map<Coordinate, Entity> parsedPositions) {
            if (Entity.Type.fromChar(ch) == Entity.Type.EMPTY)
                return;

            int x = j * 2;

            var area = new ArrayList<Coordinate>();
            area.add(new Coordinate(i, x));
            if (Entity.Type.fromChar(ch) != Entity.Type.ROBOT) {
                area.add(new Coordinate(i, x + 1));
            }

            var entity = new Entity(area, ch);
            for (var coordinate : area) {
                parsedPositions.put(coordinate, entity);
            }

            if (entity.type() == Entity.Type.ROBOT)
                this.robot = entity;
        }

        private Deque<Direction> parseDirections(String input) {
            return new ArrayDeque<>(
                    input.replaceAll("\\s", "").chars()
                            .mapToObj(ch -> Direction.getDirection((char) ch))
                            .toList()
            );
        }
    }

    @Getter
    @Accessors(fluent = true)
    private static class Entity {

        public enum Type {
            ROBOT('@'),
            WALL('#'),
            BOX('O'),
            EMPTY('.');

            private final char symbol;

            Type(char c) {
                this.symbol = c;
            }

            public char symbol() {
                return symbol;
            }

            public static Type fromChar(char c) {
                for (Type type : Type.values()) {
                    if (type.symbol() == c) {
                        return type;
                    }
                }
                throw new IllegalArgumentException("No enum constant with char: " + c);
            }
        }

        private final Type type;
        private final List<Coordinate> positions;

        @Setter
        private boolean moved = false;

        public Entity(List<Coordinate> positions, char type) {
            this.type = Type.fromChar(type);
            this.positions = positions;
        }
    }
}
