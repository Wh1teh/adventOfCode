package aoc.aoc;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;

public class App {

    private static volatile boolean inputReceived = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        var defaultTerminalFactory = new DefaultTerminalFactory();
        try (var terminal = defaultTerminalFactory.createTerminal()) {
            terminal.putCharacter('H');
            terminal.putCharacter('e');
            terminal.putCharacter('l');
            terminal.putCharacter('l');
            terminal.putCharacter('o');
            terminal.putCharacter('\n');
            terminal.flush();

            Thread.sleep(2000);
        }
    }
}
