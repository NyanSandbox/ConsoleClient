/**
 * ClientCommandHander.java
 *
 * Copyright 2019.02.01 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.handlers.UserInputHandler;
import me.nyanguymf.console.client.net.RequestManager;

/**
 * @author nyanguymf
 */
public class ClientCommandHander extends UserInputHandler {
    private Map<String, Command> commands;

    public ClientCommandHander(RequestManager out) {
        super(out);

        commands = new TreeMap<>();
    }

    @Override
    public void handle(String userInput) {
        String[] argsWithCommand = userInput.split(" ");

        if (argsWithCommand[0].length() == 1) {
            printUnknownCommand(argsWithCommand[0]);
            return;
        }

        final String commandLabel = argsWithCommand[0].substring(1, argsWithCommand[0].length());

        String[] args = Arrays.copyOfRange(argsWithCommand, 1, argsWithCommand.length);

        boolean isExecuted = false;

        for (Command cmd : commands.values()) {
            if (commandLabel.equalsIgnoreCase(cmd.getLabel())) {
                cmd.execute(args);
                isExecuted = true;
                break;
            }
        }

        if (!isExecuted)
            printUnknownCommand(commandLabel);
    }

    /** Adds command. */
    public void addCommand(Command command) {
        commands.put(command.getLabel(), command);
    }

    private void printUnknownCommand(final String label) {
        ConsoleClient.out.println("Unknown command: " + label, LinuxColor.RED_BRIGHT);
    }
}
