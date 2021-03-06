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

import nyanguymf.console.client.AuthJson;
import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.client.net.ConnectionManager;
import nyanguymf.console.common.command.ConsoleCommand;
import nyanguymf.console.common.command.ConsoleCommandExecutor;
import nyanguymf.console.common.net.Packet;
import nyanguymf.console.common.net.PacketType;

/** @author NyanGuyMF - Vasiliy Bely */
public final class LogCommand extends ConsoleCommand implements ConsoleCommandExecutor {
    private ConnectionManager conn;
    private CredentialsCache cache;

    public LogCommand(final ConnectionManager conn, final CredentialsCache cache) {
        super("log");
        super.setExecutor(this);
        this.conn  = conn;
        this.cache = cache;
    }

    @Override
    public void execute(final ConsoleCommand cmd, final String alias, final String[] args) {
        if (args.length == 0) {
            System.err.println("Not enough args! Please, use !log [enable|disable]");
            return;
        }

        Packet packet;

        switch (args[0].toLowerCase()) {
        case "enable":
            packet = new Packet.PacketBuilder()
                .body(new AuthJson(
                    cache.getLogin(),
                    cache.getPasswordHash()
                ).toJson())
                .type(PacketType.LOG_ENABLE)
                .build();
            break;
        case "disable":
            packet = new Packet.PacketBuilder()
                .body(new AuthJson(
                    cache.getLogin(),
                    cache.getPasswordHash()
                ).toJson())
                .type(PacketType.LOG_DISABLE)
                .build();
            break;

        default:
            System.err.printf("You've entered «%s», use «enable» or «disable».\n", args[0]);
            return;
        }

        conn.getOut().sendPacket(packet);
    }
}
