/**
 * Gui.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import me.nyanguymf.console.client.LinuxColor;
import me.nyanguymf.console.client.Outputable;
import me.nyanguymf.console.client.gui.events.AsyncUserInputEvent;

/**
 * @author nyanguymf
 */
@SuppressWarnings("serial")
public class Gui extends JFrame implements Outputable {
    private static AsyncUserInputEvent inputEvent;
    private static JTextPane output;
    private static JTextPane input;

    public Gui() {
        super("Console");

        super.setSize(350, 200);
        // centering
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        output = new JTextPane();
        output.setBackground(Color.BLACK);
        output.setEditable(false);

        inputEvent = new AsyncUserInputEvent();

        input = new JTextPane();
        input.setBackground(Color.BLACK);
        input.setForeground(Color.GREEN);
        input.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != 10)
                    return;

                Component c = e.getComponent();

                if (!(c instanceof JTextPane))
                    return;

                JTextPane input = (JTextPane) c;
                String inputString = input.getText().replace('\n', '\0').trim();

                input.setText("");

                inputEvent.setInput(inputString);

                new Thread(inputEvent).start();
            }
            public void keyPressed(KeyEvent e) {
            }
        });

        JScrollPane scrollable = new JScrollPane(output);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2, 2));
        container.add(scrollable);
        container.add(input);
    }

    /**
     * Prints message to GUI output.
     *
     * @param msg Message show.
     * @param c Message's color.
     */
    private void print(String msg, Color c) {
        output.setEditable(true);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = output.getDocument().getLength();
        output.setCaretPosition(len);
        output.setCharacterAttributes(aset, false);
        output.replaceSelection(msg);
        output.setEditable(false);

        input.setText("");
    }

    public void print(String msg) {
        print(msg, Color.GREEN);
    }

    public void println(String msg) {
        println(msg, Color.GREEN);
    }

    private void println(String msg, Color c) {
        print(msg + '\n', c);
    }

    public void registerInputListener(Observer o) {
        inputEvent.addObserver(o);
    }

    public void close() {
        setVisible(false);
    }

    /**
     * It doesn't provide colors.
     */
    @Deprecated
    @Override
    public void print(String message, LinuxColor... color) {
        print(message);
    }

    /**
     * It doesn't provide colors.
     */
    @Deprecated
    @Override
    public void println(String message, LinuxColor... color) {
        println(message);
    }
}
