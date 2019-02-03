/**
 * ExitExecutor.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands.executors;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.commands.Command;

/**
 * @author nyanguymf
 */
public class ExitExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(Command cmd, String label, String[] args) {
        ConsoleClient.out.println("Bye-bye, sweety c:", LinuxColor.CYAN_BRIGHT);
        ConsoleClient.exit(ConsoleClient.SUCCESS);
        return true;
    }
}
