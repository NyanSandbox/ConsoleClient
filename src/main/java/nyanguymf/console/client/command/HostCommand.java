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
package nyanguymf.console.client.command;

import static java.lang.Integer.parseInt;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.common.command.ConsoleCommand;
import nyanguymf.console.common.command.ConsoleCommandExecutor;

/** @author NyanGuyMF - Vasiliy Bely */
public final class HostCommand extends ConsoleCommand implements ConsoleCommandExecutor {
    private CredentialsCache cache;

    public HostCommand(final CredentialsCache cache) {
        super("host");
        super.setExecutor(this);

        this.cache = cache;
    }

    @Override
    public void execute(final ConsoleCommand cmd, final String alias, final String[] args) {
        if (args.length == 0) {
            System.out.println("Use !host «new host», please");
            return;
        }

        // split host:port
        String[] hostPort = args[0].split(":");
        String newHost = hostPort[0];
        int newPort;

        if (hostPort.length == 1) {
            System.out.println("Warning: you've entered only host.");
            System.out.println("Port value will not change.");
            newPort = cache.getPort();
        } else {
            newPort = setNewPort(hostPort[1]);
        }

        if (newHost.equals(cache.getHost())) {
            System.out.println("You've entered old host.");
            return;
        }

        cache.setHost(newHost);
        System.out.printf("New host «%s:%d» has been successfuly set.\n", newHost, newPort);
    }

    private int setNewPort(final String port) {
        int newPort = cache.getPort();

        try {
            newPort = parseInt(port);
        } catch (NumberFormatException ex) {
            System.err.println("You should enter the number between 0 and 65535 for port!");
        }

        cache.setPort(newPort);
        return newPort;
    }
}
