/**
 * ServerInputEvent.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.net.events;

import java.util.Observable;

import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;

/**
 * @author nyanguymf
 */
public class ServerInputEvent extends Observable {
    private Packet packet;

    // TODO: implement async notify through Runnable.

    /**
     * Sets packet to event.
     * <p>
     * Implicitly calls {@link Observable#setChanged()} method.
     *
     * @param packet : {@link Packet} to set.
     */
    public void setPacket(Packet packet) {
        this.packet = packet;
        super.setChanged();
    }

    public PacketType getType() {
        return packet.getType();
    }

    public String getBody() {
        return packet.getBody();
    }

    public Object getMisc() {
        return packet.getMisc();
    }
}
