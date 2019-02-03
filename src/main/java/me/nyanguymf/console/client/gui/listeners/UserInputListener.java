/**
 * UserInputListener.java 2019.01.29
 * 
 * @author NyanGuyMF
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.gui.listeners;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.commands.ClientCommandHander;
import me.nyanguymf.console.client.commands.Command;
import me.nyanguymf.console.client.net.RequestManager;
import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;

/**
 * @author nyanguymf
 */
public class UserInputListener implements Observer {
    private ClientCommandHander commandHandler;
    private RequestManager out;

    public UserInputListener(RequestManager out) {
        this.out = out;
        this.commandHandler = new ClientCommandHander(out);
    }

    @Override
    public void update(Observable o, Object arg) {
        String userInput = (String) arg;

        /*---------------------------------------*
         *    Here we handle client commands.    *
         *---------------------------------------*/
        if (userInput.startsWith("/")) {
            commandHandler.handle(userInput);
            return;
        }

        /*--------------------------------------*
         *   If user's input isn't client cmd   *
         * we create new command packet request,*
         * but first of all we must sure client *
         *      is authorized for server.       *
         *--------------------------------------*/

        if (!ConsoleClient.isAuthorized()) {
            ConsoleClient.out.println("You're not authorized!", LinuxColor.RED_BRIGHT);
            return;
        }

        Packet commandPacket = new Packet();
        commandPacket.setBody(userInput);
        commandPacket.setSender(ConsoleClient.getCurrentProfile().getUserName());
        commandPacket.setType(PacketType.COMMAND_PACKET);

        /*--------------------------------*
         *    And sending that packet.    *
         *--------------------------------*/
        try {
            out.sendRequest(commandPacket);
        } catch (IOException e) {
            ConsoleClient.out.println("Command sending failed.", LinuxColor.RED_BRIGHT);
        }
    }

    /**
     * Adds command to handler.
     * {@link ClientCommandHander#addCommand(Command)}
     */
    public void addCommand(Command cmd) {
        this.commandHandler.addCommand(cmd);
    }
}
