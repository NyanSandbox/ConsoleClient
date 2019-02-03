/**
 * ConnectionManager.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

import javax.swing.JOptionPane;

import me.nyanguymf.console.client.data.Profile;
import me.nyanguymf.console.net.Packet;
import me.nyanguymf.console.net.PacketType;

/**
 * @author nyanguymf
 */
public class ConnectionManager {
    private final Profile profile;
    private Socket connection;
    private InputManager in;
    private OutputManager out;

    /**
     * Creates {@link Socket} connection with I/O managers.
     *
     * @param profile : {@link Profile} with connection settings.
     * @throws IOException : In the case of unable to create I/O handlers.
     */
    public ConnectionManager(final Profile profile) throws IOException {
        this.profile = profile;

        try {
            connection = new Socket(profile.getHost(), profile.getPort());
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Couldn't connect to the server «"
                                                + profile.getHost() + "»"
                                                + "\nRestart program with correct "
                                                + " configuration."
                                                , "Unknown host"
                                                , JOptionPane.ERROR_MESSAGE);
            connection = null;
        } catch (ConnectException ex) {
            JOptionPane.showMessageDialog(null, "Ensure you have started your"
                                                + " Bukkit server with plug-in."
                                                , "Couldn't connect to the server"
                                                , JOptionPane.ERROR_MESSAGE);
            connection = null;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unknown error while connection to the server."
                                                + "\nSee console and restart program "
                                                + "with correct configuration."
                                                , "Error"
                                                , JOptionPane.ERROR_MESSAGE);
            connection = null;
        }

        if (connection == null)
            return;

        in  = new InputManager(connection.getInputStream());
        out = new OutputManager(connection.getOutputStream());
    }

    /** Register {@link Observer} for {@link InputManager}. */
    public void registerInputListener(Observer obs) {
        in.registerListener(obs);
    }

    public synchronized RequestManager getRequestManager() {
        return this.out;
    }

    /**
     * TODO: move to some handler in the future.
     * <br>
     * Sends password verification to the server and gets server
     * response. If password isn't valid - method will request
     * user for a new password and retry connection.
     * <p>
     * If tries are more than 3 server will not block user's IP
     * for N seconds.
     * (N configured on server)
     *
     * @param password : The password to connect.
     * @return is password valid.
     */
    @Deprecated
    public boolean enterPassword(String password, String userName) {
        Packet packet = new Packet(PacketType.AUTH_PACKET, password, userName);
        Bukkit.getConsoleSender().sendMessage("Sending packet: " + packet.toString());

        try {
            out.sendRequest(packet);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Closes I/O and {@link Socket} connection.*/
    public void close() {
        try {
            in.interrupt();
            in.close();
            out.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Gets profile. */
    public Profile getProfile() {
        return this.profile;
    }
}
