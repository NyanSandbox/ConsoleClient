/**
 * Outputable.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client;

/**
 * @author nyanguymf
 */
public interface Outputable {

    /** Prints message to output. */
    public void print(String message);
    
    /** Prints colored message with. */
    public void print(String message, LinuxColor... color);

    /** Prints message to output with new line character. */
    public void println(String message);

    /** Prints colored message with new line character. */
    public void println(String message, LinuxColor... color);
}
