/**
 * PacketHandler.java
 *
 * Copyright 2019.02.04 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.client.net.handlers;

import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
@FunctionalInterface
public interface PacketHandler {

    /**
     * Handles {@link Packet}.
     *
     * @param packet {@link Packet} to handle.
     */
    public void handle(Packet packet);
}
