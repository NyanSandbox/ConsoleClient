/**
 * UserInputEvent.java 2019.01.29
 * 
 * @author NyanGuyMF
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.gui.events;

import java.util.Observable;

/**
 * @author nyanguymf
 */
public class AsyncUserInputEvent extends Observable implements Runnable {
    private String input;

    public void run() {
        super.setChanged();
        super.notifyObservers(input);
        super.clearChanged();
    }

    public void setInput(String input) {
        this.input = input;
    }
}
