/**
 * OutputManager.java
 *
 * Copyright 2019.01.30 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
class OutputManager implements RequestManager {
    private ObjectOutputStream out;

    public OutputManager(OutputStream out) throws IOException {
        this.out = new ObjectOutputStream(out);
    }

    /** Sends request to server. */
    public void sendRequest(Packet request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }
}
