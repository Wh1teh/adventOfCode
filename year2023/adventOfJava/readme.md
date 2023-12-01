# AoC 2023 with Java

## Build

### Requirements

Maven 3
Java 20

But you might be able to get away with changing the pom.xml if you'd prefer to user older versions.

### Compile and Run

after cd to adventOfJava folder:

```
mvn install
mvn exec:java  -Dexec.mainClass=com.aoc.AdventOfCode -Dexec.args="0 0"
```

`-Dexec.args="<day(0-25)> <part(0/1/2)>"` Change args depending on which answer you want to get
