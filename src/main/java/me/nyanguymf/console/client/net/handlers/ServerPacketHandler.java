/**
 * ServerInputHandler.java
 *
 * Copyright 2019.01.30 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.client.net.handlers;

import java.util.Observable;
import java.util.Observer;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.net.events.ServerInputEvent;
import me.nyanguymf.console.net.PacketType;

/**
 * @author nyanguymf
 *
 */
public class ServerPacketHandler implements Observer {
    private PacketHandler infoPacketHandler;

    public ServerPacketHandler() {
        this.infoPacketHandler = new InfoPacketHandler();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof ServerInputEvent)) {
            ConsoleClient.out.println("[ServerInputHander] Got unexpected event"
                                        , LinuxColor.RED_BRIGHT);
            return;
        }

        ServerInputEvent event = (ServerInputEvent) o;

        PacketType type = event.getType();

        // TODO: implement
        switch(type) {
        case AUTH_ERROR:
            ConsoleClient.out.println(event.getBody(), LinuxColor.RED_BRIGHT);
            break;
        case AUTH_NEEDED:
            ConsoleClient.out.println("Сервер запрашивает авторизацию."
                                        + "Введите пароль /pass «ваш пароль»");
            break;
        case AUTH_PACKET:
            // Server doesn't need authentification
            // TODO: INVALID_PACKET_ERROR answer.
            break;
        case AUTH_SUCCESS:
            ConsoleClient.out.println(event.getBody());
            ConsoleClient.setAuthorized(true);
            break;
        case COMMAND_PACKET:
            // TODO: INVALID_PACKET_ERROR answer.
            // Nothing to execute on client side.
            break;
        case INFO:
            infoPacketHandler.handle(event.getPacket());
            break;
        case INVALID_PACKET_ERROR:
            ConsoleClient.out.println("Server got invalid packet: "
                                        + event.getMisc().toString());
            break;

        default:
            // TODO: INVALID_PACKET_ERROR answer.
            ConsoleClient.out.println("Got unexpected packet type: " + event.getType()
                                       , LinuxColor.RED_BRIGHT);
            break;
        }
    }

}
