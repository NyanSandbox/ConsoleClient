package nyanguymf.console.client;

import static java.util.Arrays.copyOfRange;

import java.util.Scanner;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.client.command.CommandManager;
import nyanguymf.console.client.command.HelpCommand;
import nyanguymf.console.client.command.HostCommand;
import nyanguymf.console.client.command.LoginCommand;
import nyanguymf.console.client.command.PasswordCommand;
import nyanguymf.console.client.command.PortCommand;
import nyanguymf.console.client.command.ReconnectCommand;
import nyanguymf.console.client.command.StopCommand;
import nyanguymf.console.client.io.ClientInputManager;
import nyanguymf.console.client.net.ConnectionManager;

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
        exit(status);
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

        commandManager = new CommandManager();
        commandManager.registerCommand(new StopCommand());
        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new LoginCommand(credentialsCache));
        commandManager.registerCommand(new PasswordCommand(credentialsCache));
        commandManager.registerCommand(new HostCommand(credentialsCache));
        commandManager.registerCommand(new PortCommand(credentialsCache));
        commandManager.registerCommand(new ReconnectCommand(
                credentialsCache, connectionManager
        ));

        clientInput = new ClientInputManager(sc);
        clientInput.getEvent().register((event) -> {
            String[] input = event.getInput().split("\\s");
            String   name  = input[0].replaceFirst("/", "");
            String[] args  = input.length > 1
                    ? copyOfRange(input, 1, input.length)
                    : new String[0];

            commandManager.executeCommand(name, args);
        });

        clientInput.start();
        System.out.println("Type /help for more info.");
    }

    public boolean isAlive() {
        return clientInput.isAlive() || connectionManager.getIn().isAlive();
    }

    private void onDisable() {
        connectionManager.close();
    }
}
