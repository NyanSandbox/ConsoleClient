/**
 * ConsoleOutput.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 *
 * @version 1.0
 */
package me.nyanguymf.console.client;

import java.util.Arrays;

/**
 * @author nyanguymf
 */
class ConsoleOutput implements Outputable {
    private boolean isUnix = false;

    public ConsoleOutput() {
        this.isUnix = ConsoleClient.isUnix();
    }

    /*-----------------------------------------------------*
     * Info for developers: colors displays incorrectly in *
     *       your IDE console, use standard instead.       *
     *-----------------------------------------------------*/

    @Override
    public void print(String message) {
        if (isUnix)
            System.out.print(LinuxColor.CYAN_BRIGHT + message + LinuxColor.RESET);
        else
            System.out.print(message);
    }

    @Override
    public void print(String message, LinuxColor... color) {
        if (isUnix) {
            StringBuffer colors = new StringBuffer("");
            Arrays.stream(color).forEach(action -> colors.append(action));

            System.out.print(colors + message + LinuxColor.RESET);
        } else {
            System.out.print(message);
        }
    }

    @Override
    public void println(String message) {
        if (isUnix)
            System.out.println(LinuxColor.CYAN_BRIGHT + message + LinuxColor.RESET);
        else
            System.out.println(message);
    }

    @Override
    public void println(String message, LinuxColor... color) {
        if (isUnix) {
            StringBuffer colors = new StringBuffer("");
            Arrays.stream(color).forEach(action -> colors.append(action));

            System.out.println(colors + message + LinuxColor.RESET);
        } else {
            System.out.println(message);
        }
    }
}
