# AoC 2023 with Java

## Build

### Requirements

* Maven 3
* JDK 20

But you might be able to get away with changing the pom.xml if you'd prefer to use older versions.

### Compile and Run

after cd to adventOfJava folder:

```
mvn install
mvn exec:java  -Dexec.mainClass=com.aoc.AdventOfCode -Dexec.args="0 0 1"
```
Change args depending on which answer you wish to see, or no args if you wish to play all days:

```-Dexec.args="<day(0-25)> <part(0/1/2) <rounds>"```

You can specify -1 days to play through all of the days and parts. Rounds specifies how many times each puzzle is played for benchmarking.
