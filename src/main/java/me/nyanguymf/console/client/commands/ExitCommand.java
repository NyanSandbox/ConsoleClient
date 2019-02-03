/**
 * ExitCommand.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;

/**
 * @author nyanguymf
 */
public final class ExitCommand extends Command {

    /** Exits from program. */
    public ExitCommand(String label, String usage) {
        super(label, usage);
    }

    public void handle(String userInput) {
        ConsoleClient.out.println("Bye-bye, sweety :з", LinuxColor.MAGENTA_BRIGHT);
        ConsoleClient.exit(ConsoleClient.SUCCESS);
    }
}
