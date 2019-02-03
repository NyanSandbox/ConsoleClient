/**
 * UserInputEvent.java
 *
 * Copyright 2019.02.02 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.events;

import java.io.InputStream;
import java.util.Observable;
import java.util.Scanner;

/**
 * @author nyanguymf
 */
public class AsyncUserInputEvent extends Observable implements Runnable {
    private volatile Scanner input;

    public AsyncUserInputEvent(InputStream inputStream) {
        this.input = new Scanner(inputStream);
    }

    @Override
    public void run() {
        String line;
        while (true) {
            if (Thread.currentThread().isInterrupted())
                break;

            line = input.nextLine();
            super.setChanged();
            super.notifyObservers(line);
        }
    }
}
