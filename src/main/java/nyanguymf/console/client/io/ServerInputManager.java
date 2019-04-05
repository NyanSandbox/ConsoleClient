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
package nyanguymf.console.client.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.common.net.Packet;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ServerInputManager extends Thread {
    private InputStream serverInputStream;
    private ServerPacketEvent event;
    private ObjectInputStream in;

    public ServerInputManager(
            final InputStream inputStream,
            final CredentialsCache cache
    ) throws IOException {
        super("Server input thread");

        serverInputStream = inputStream;
        event = new ServerPacketEvent(new ServerPacketHandler(cache));
    }

    @Override public void run() {
        try {
            in = new ObjectInputStream(serverInputStream);
        } catch (IOException ex) {
            System.err.println("Unable to establish input connection with server.");
            return;
        }

        Object obj = null;

        while (!currentThread().isInterrupted()) {
            try {
                obj = in.readObject();

                if (!(obj instanceof Packet)) {
                    System.err.println("Got invalid packet from server.");
                    continue;
                }

                Packet packet = (Packet) obj;
                new Thread(
                    event.setPacket(packet),
                    "Handle server packet thread."
                ).start();

            } catch (ClassNotFoundException | InvalidClassException ex) {
                ex.printStackTrace();
                System.err.println("Got invalid packet from server.");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException expected) {
                // Unable to establish input connection
            }
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ignore) {}
    }
}
