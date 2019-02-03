/**
 * PasswordExecutor.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.commands.executors;

import java.io.IOException;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.commands.Command;
import me.nyanguymf.console.client.net.RequestManager;
import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;

/**
 * @author nyanguymf
 *
 */
public class PasswordExecutor implements CommandExecutor {
    private RequestManager out;

    public PasswordExecutor(RequestManager out) {
        this.out = out;
    }

    @Override
    public boolean onCommand(Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String userName = ConsoleClient.getCurrentProfile().getUserName();
        Packet passPacket = new Packet(PacketType.AUTH_PACKET, args[0], userName);

        try {
            out.sendRequest(passPacket);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return true;
    }

}
