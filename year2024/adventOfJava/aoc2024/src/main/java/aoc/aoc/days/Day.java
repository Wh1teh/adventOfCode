package aoc.aoc.days;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class Day implements IDay {

    private final String dayOrdinal;

    protected Day() {
        String className = this.getClass().getSimpleName();
        this.dayOrdinal = className.substring(className.length() - 2);
    }

    @SneakyThrows
    protected String read(Integer part) {
        String dayFileName = String.format("day%s_%d", dayOrdinal, part);
        Path path = Paths.get(getDayDataPath().toString(), dayFileName);
        return Files.readString(path);
    }

    @SneakyThrows
    private static Path getDayDataPath() {
        var resource = Objects.requireNonNull(
                Day.class.getClassLoader().getResource("daydata"),
                "'daydata' folder not found in classpath"
        );

        // Convert the resource URL to a Path object
        return Paths.get(resource.toURI());
    }
}
