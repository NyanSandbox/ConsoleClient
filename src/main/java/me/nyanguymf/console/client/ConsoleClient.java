/**
 * Application.java 2019.01.29
 *
 * @author NyanGuyMF
 *
 * @version 1.0
 */

package me.nyanguymf.console.client;

import java.io.IOException;

import javax.swing.JOptionPane;

import me.nyanguymf.console.client.commands.ExitCommand;
import me.nyanguymf.console.client.commands.PasswordCommand;
import me.nyanguymf.console.client.commands.executors.ExitExecutor;
import me.nyanguymf.console.client.commands.executors.PasswordExecutor;
import me.nyanguymf.console.client.data.Configuration;
import me.nyanguymf.console.client.data.Profile;
import me.nyanguymf.console.client.events.AsyncUserInputEvent;
import me.nyanguymf.console.client.gui.Gui;
import me.nyanguymf.console.client.gui.listeners.UserInputListener;
import me.nyanguymf.console.client.net.ConnectionManager;
import me.nyanguymf.console.client.net.handlers.ServerPacketHandler;

/**
 * Console Client for Bukkit servers.
 * <p>
 * You have to install my plug-in on
 * your Bukkit server and configure it
 * to make this console work.
 * <p>
 * I didn't thought about console at first,
 * but then I looked at my GUI and understood,
 * that it's too bad. :D
 * Because of this there are a lot of not
 * high quality code here, but it's OK.
 * <p>
 * This project uses GPL v3.
 * Make sure you added all yours @author
 * javadocs under your changes.
 * <p>
 * I'm trying follow
 * <a href="http://google.github.io/styleguide/javaguide.html">Google<a>
 * and
 * <a href="https://www.oracle.com/technetwork/java/codeconventions-150003.pdf">Oracle<a>
 * code conventions, please, read it it apply in
 * your code in this project, if you want to contribute.
 *
 * @author NyanGuyMF
 */
public class ConsoleClient {
    private static Configuration config;
    private static ConnectionManager conn;
    private static Gui gui;
    private static Profile currentProfile;
    private static Thread inputThread;

    /** Is client authorized for server. */
    private static boolean isAuthorized = false;

    public static final short SUCCESS = 0;
    public static final short CONNECTION_ERROR = 1;
    public static final short IO_CONNECTION_ERROR = 2;
    public static final short CONFIG_LOAD_ERROR = 3;
    public static final short CONFIG_SAVE_ERROR = 4;

    public static Outputable out;

    /** Entry point. */
    // TODO: some arguments c:
    public static void main(String[] args) {
        int consoleOrGui = JOptionPane.showOptionDialog(null
                                    , "Should I load program in console?"
                                    , "Console vs GUI"
                                    , JOptionPane.YES_NO_OPTION
                                    , JOptionPane.QUESTION_MESSAGE
                                    , null /* Maybe we need some icons? */
                                    , null
                                    , null);

        if (consoleOrGui == JOptionPane.YES_OPTION)
            init(true);
        else
            init(false);
    }

    /** Gets auth status for Client. */
    public static boolean isAuthorized() {
        return isAuthorized;
    }

    /** Sets authorized status. */
    public static void setAuthorized(boolean isAuthorized) {
        ConsoleClient.isAuthorized = isAuthorized;
    }

    /** Exit from program. Calls {@link #stop()} */
    public static void exit(short code) {
        stop();
        System.exit(code);
    }

    public static boolean isUnix() {
        return !System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /** Closes connections, interrupts threads and closes files. */
    private static void stop() {
        if (gui != null)
            gui.close();

        if (config != null)
            config.unload("config");

        if (conn != null)
            conn.close();

        if (inputThread != null)
            inputThread.interrupt();
    }

    /**
     * Initialization for client.
     * <p>
     * Calls configuration loader {@link #loadConfig()},
     * GUI loader if need{@link #loadGui()}, {@link #initConn(boolean)},
     * registers {@link UserInputListener} with
     * {@link #registerInputListener(boolean)}.
     *
     * @param isConsole : Console is <tt>true</tt>
     * and GUI - <tt>false</tt>.
     */
    private static void init(boolean isConsole) {
        loadConfig();

        if (!isConsole) {
            loadGui();
            out = gui;
        } else {
            // TODO: greeting
            /* Is it correctly? */
            out = new ConsoleOutput();
        }

        currentProfile = config.getProfile("default");

        initConn();
        conn.registerInputListener(new ServerPacketHandler());

        registerInputListener(isConsole);
    }

    /**
     * Registers user input listener for GUI or console.
     * <p>
     * If user selected GUI - this method will register listener
     * for GUI else for console.
     *
     * @param isConsole : Console is <tt>true</tt>
     * and GUI - <tt>false</tt>.
     */
    private static void registerInputListener(boolean isConsole) {
        UserInputListener inputListener = new UserInputListener(conn.getRequestManager());

        /*--------------------------------*
         * Register commands for listener *
         * maybe should create new method *
         *--------------------------------*/
        PasswordCommand passCmd = new PasswordCommand("pass", "/pass «password»");
        passCmd.setExecutor(new PasswordExecutor(conn.getRequestManager()));
        inputListener.addCommand(passCmd);

        ExitCommand exitCmd = new ExitCommand("exit", "/exit");
        exitCmd.setExecutor(new ExitExecutor());
        inputListener.addCommand(exitCmd);

        /*-------------------------*
         * Apply listener to input *
         *-------------------------*/
        if (!isConsole) {
            gui.registerInputListener(inputListener);
        } else {
            AsyncUserInputEvent inputEvent = new AsyncUserInputEvent(System.in);
            inputEvent.addObserver(inputListener);
            inputThread = new Thread(inputEvent);
            inputThread.start();
        }
    }

    /**
     * Loads configuration.
     * <p>
     * May cause exception and exit from programm with codes:
     * <ul>
     *  <li>{@link #CONFIG_LOAD_ERROR}</li>
     *  <li>{@link #CONFIG_SAVE_ERROR}</li>
     * </ul>
     */
    private static void loadConfig() {
        config = new Configuration("config.json");
    }

    /**
     * Loads GUI if needed and sets if visible.
     * <p>
     * At this point GUI looks really bad, couldn't
     * somebody help me with this shit? ':D
     */
    private static void loadGui() {
        gui = new Gui();
        gui.setVisible(true);
    }

    /**
     * Initialize {@link ConnectionManager}.
     * <p>
     * May cause errors while loading and connecting
     * to server with codes:
     * <ul>
     *  <li>{@link #CONNECTION_ERROR} - possibly server doesn't enabled.</li>
     *  <li>{@link #IO_CONNECTION_ERROR} - possibly ports errors.</li>
     * </ul>
     */
    private static void initConn() {
        try {
            conn = new ConnectionManager(currentProfile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Exception occurred while 'handshaking' with server I/O.");
            ConsoleClient.exit(IO_CONNECTION_ERROR);
        }
    }

    /**
     * @return the currentProfile
     */
    public static Profile getCurrentProfile() {
        return currentProfile;
    }
}
