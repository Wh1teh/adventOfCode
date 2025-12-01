package aoc.aoc.days.interfaces;

import aoc.aoc.days.enums.Part;

public interface DaySpecifier {

    DaySpecifier setSample(boolean isSample);

    DaySpecifier setPart(Part part);
}
