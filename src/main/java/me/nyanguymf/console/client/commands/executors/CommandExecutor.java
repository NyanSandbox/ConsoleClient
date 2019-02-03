/**
 * CommandExecutor.java
 *
 * Copyright 2019.02.01 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands.executors;

import me.nyanguymf.console.client.commands.Command;

/**
 * @author nyanguymf
 */
public interface CommandExecutor {

    /**
     * It is performed when the {@link Command} was run.
     *
     * @param cmd : Running command.
     * @param label : Command's label / name.
     * @param args : Command's arguments.
     * @return Whether the command is executed.
     *
     * @see Command
     */
    public boolean onCommand(final Command cmd, final String label, final String[] args);
}
