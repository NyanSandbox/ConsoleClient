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

import static java.util.Arrays.asList;

import java.util.HashSet;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.client.net.ConnectionManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ReconnectCommand extends Command implements CommandExecutor {
    private CredentialsCache cache;
    private ConnectionManager conn;

    public ReconnectCommand(final CredentialsCache cache, final ConnectionManager conn) {
        super("reconnect", new HashSet<>(asList("reconn")));

        super.setExecutor(this);
        this.cache = cache;
        this.conn  = conn;
    }

    @Override
    public void execute(final Command cmd, final String alias, final String[] args) {
        try {
            conn.connect(cache);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        switch (conn.getStatus()) {
        case CONNECTED:
            System.out.println("Connected!");
            break;
        case INVALID_PORT:
            System.out.println("Invalid port!");
            break;
        case UNKNOWN_HOST:
            System.out.println("You've entered invalid host.");
            break;
        case CONNECTION_REFUSED:
            System.out.println("Connection refused.");
            break;
        }
    }
}
