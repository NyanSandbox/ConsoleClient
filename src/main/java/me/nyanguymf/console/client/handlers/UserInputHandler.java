/**
 * PacketHandler.java
 *
 * Copyright 2019.01.31 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.handlers;

import me.nyanguymf.console.client.net.RequestManager;

/**
 * @author nyanguymf
 */
public abstract class UserInputHandler implements Handler {
    private RequestManager out;

    public UserInputHandler(RequestManager out) {
        this.out = out;
    }

    protected RequestManager getRequestManager() {
        return this.out;
    }
}
