/**
 * InputThread.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.Observer;

import javax.swing.JOptionPane;

import me.nyanguymf.console.client.ConsoleClient;
import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.net.events.ServerInputEvent;
import me.nyanguymf.console.net.Packet;

/**
 * @author nyanguymf
 */
class InputManager extends Thread {
    private volatile ServerInputEvent event;
    private InputStream is;
    private ObjectInputStream in;

    public InputManager(InputStream inputSream) {
        is = inputSream;
        event = new ServerInputEvent();

        start();
    }

    public void registerListener(Observer obs) {
        event.addObserver(obs);
    }

    @Override
    public void run() {
        try {
            Packet packet;
            in = new ObjectInputStream(is);
            while (true) {
                if (super.isInterrupted() || !super.isAlive())
                    return;

                 packet = getResponse();

                if (packet == null)
                    continue;

                event.setPacket(packet);
                event.notifyObservers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        in.close();
    }

    /**
     * Gets server response.
     * <p>
     * Returns <tt>null</tt> if exception happened.
     *
     * @return Server's response {@link Packet} or <tt>null</tt>.
     */
    private Packet getResponse() {
        try {
            Object obj = null;
            try {
                obj = in.readObject();
            } catch (EOFException expected) {
                ConsoleClient.out.println("Lost connection.", LinuxColor.RED_BRIGHT);
                Thread.currentThread().interrupt();
                return null;
            } catch (SocketException expected) {
                ConsoleClient.out.println("Lost connection.", LinuxColor.RED_BRIGHT);
                Thread.currentThread().interrupt();
                return null;
            }

            if (!(obj instanceof Packet))
                throw new ClassNotFoundException();

            return (Packet) obj;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Got invalid object from server.\nIs it up to date?", null, JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
