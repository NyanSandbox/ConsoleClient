/**
 * InfoPacketHandler.java
 *
 * Copyright 2019.02.04 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.client.net.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
class InfoPacketHandler implements PacketHandler {
    private final String regex;
    private final Pattern pattern;

    public InfoPacketHandler() {
        this.regex   = "(\\[(\\d){1,2}([^0-9])?)(;{1}(\\d){1,2}([^0-9\\W])?)+";
        this.pattern = Pattern.compile(regex, Pattern.MULTILINE);
    }

    @Override
    public void handle(Packet packet) {
        if (ConsoleClient.isUnix()) {
            ConsoleClient.out.print(packet.getBody(), LinuxColor.CYAN_BRIGHT);
            return;
        }

        /*------------------------*
         *  Is there another way  *
         * to represent ESC char? *
         *------------------------*/
        String message = packet.getBody().replace((char) 27 + "", "");

        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            message = message.replace(matcher.group(0), "");
        }

        if (message.substring(message.length() - 3).equalsIgnoreCase("[m\n"))
            message = message.substring(0, message.length() - 3) + '\n';

        /* Print message without colors. */
        ConsoleClient.out.print(message);
    }
}
