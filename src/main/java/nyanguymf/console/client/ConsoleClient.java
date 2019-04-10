/**
 * This file is the part of Console Client program.
 *
 * Copyright (c) 2019 Vasily
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package nyanguymf.console.client;

import java.util.Scanner;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.client.command.ExitCommand;
import nyanguymf.console.client.command.HelpCommand;
import nyanguymf.console.client.command.HostCommand;
import nyanguymf.console.client.command.LogCommand;
import nyanguymf.console.client.command.LoginCommand;
import nyanguymf.console.client.command.PasswordCommand;
import nyanguymf.console.client.command.PortCommand;
import nyanguymf.console.client.command.ReconnectCommand;
import nyanguymf.console.client.io.ClientInputManager;
import nyanguymf.console.client.net.ConnectionManager;
import nyanguymf.console.common.command.CommandManager;

public class ConsoleClient {
    public static final short SUCCESS = 0;
    public static final short CONNECTION_ERROR = 1;
    private static ConsoleClient instance;

    private CommandManager commandManager;
    private ConnectionManager connectionManager;
    private CredentialsCache credentialsCache;
    private ClientInputManager clientInput;

    public static void main(final String[] args) {
        ConsoleClient.instance = new ConsoleClient();
    }

    public static void exit(final short status) {
        ConsoleClient.instance.onDisable();
        System.out.println("Bye-bye)");
        System.exit(status);
    }

    private ConsoleClient() {
        Scanner sc = new Scanner(System.in);
        credentialsCache = new CredentialsCache(sc).initialize();
        try {
            connectionManager = new ConnectionManager().connect(credentialsCache);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Unable to connect to server, exiting");
            exit(ConsoleClient.CONNECTION_ERROR);
        }

        switch (connectionManager.getStatus()) {
        case CONNECTED:
            System.out.println("Connected!");
            break;
        case INVALID_PORT:
            System.out.println("Invalid port!");
            break;
        case UNKNOWN_HOST:
            System.out.println("You've entered invalid host.");
            break;
        case CONNECTION_REFUSED:
            System.out.println("Connection refused.");
            break;
        }

        commandManager = new CommandManager(connectionManager, credentialsCache);
        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new LoginCommand(credentialsCache));
        commandManager.registerCommand(new PasswordCommand(credentialsCache));
        commandManager.registerCommand(new HostCommand(credentialsCache));
        commandManager.registerCommand(new PortCommand(credentialsCache));
        commandManager.registerCommand(new ExitCommand());
        commandManager.registerCommand(new LogCommand(
                connectionManager, credentialsCache
        ));
        commandManager.registerCommand(new ReconnectCommand(
                credentialsCache, connectionManager
        ));

        clientInput = new ClientInputManager(sc, commandManager);

        clientInput.start();
        System.out.println("Type /help for more info.");
    }

    public boolean isAlive() {
        return clientInput.isAlive() || connectionManager.getIn().isAlive();
    }

    private void onDisable() {
        try {
            connectionManager.close();
        } catch (NullPointerException ignore) {}
        System.out.println("Console client was disabled.");
    }
}
