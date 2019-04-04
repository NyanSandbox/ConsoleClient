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

import java.util.Scanner;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ClientInputManager extends Thread {
    private static boolean isIgnoring = false;
    private ClientInputEvent event;
    private Scanner in;

    public ClientInputManager(final Scanner in) {
        super("Client input thread.");

        this.in = in;
        event = new ClientInputEvent();
    }

    @Override public void run() {
        while (!currentThread().isInterrupted()) {
            String command;
            command = in.nextLine();

            if (ClientInputManager.isIgnoring) {
                continue;
            }

            if (!command.startsWith("/")) {
                System.out.println("Enter /help for more information.");
                continue;
            }

            event.setInput(command);
            new Thread(
                event,
                "Client input handler"
            ).start();
        }
    }

    /** @return the event */
    public ClientInputEvent getEvent() {
        return event;
    }

    public void close() {
        in.close();
        currentThread().interrupt();
    }

    /** @return the isIgnoring */
    public boolean isIgnoring() {
        return ClientInputManager.isIgnoring;
    }

    /** Sets isIgnoring */
    public static void setIgnoring(final boolean isIgnoring) {
        ClientInputManager.isIgnoring = isIgnoring;
    }
}
