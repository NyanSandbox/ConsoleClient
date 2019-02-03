/**
 * Command.java
 *
 * Copyright 2019.02.01 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.commands.executors.CommandExecutor;

/**
 * @author nyanguymf
 */
public abstract class Command {
    private String label;
    private CommandExecutor executor;
    private String usage;

    /**
     * Creates new command with given label (also name).
     * @param usage : Command's usage tooltip.
     */
    public Command(final String label, final String usage) {
        this.label = label;
        this.usage = usage;
    }
    
    /**
     * Executes command.
     *
     * @param args : Command's arguments.
     *
     * @see CommandExecutor#onCommand(Command, String, String[])
     */
    public void execute(final String[] args) {
        if (!this.executor.onCommand(this, getLabel(), args))
            ConsoleClient.out.println("Use: " + usage, LinuxColor.RED_BRIGHT);
    }

    /** Sets command's {@link CommandExecutor} */
    public void setExecutor(final CommandExecutor executor) {
        this.executor = executor;
    }
    
    /** Gets command's label / name. */
    protected String getLabel() {
        return this.label;
    }
}
