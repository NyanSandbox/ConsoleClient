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

/** @author NyanGuyMF - Vasiliy Bely */
public final class PasswordCommand extends Command implements CommandExecutor {
    private CredentialsCache cache;

    public PasswordCommand(final CredentialsCache cache) {
        super("password", new HashSet<>(asList("pass")));
        super.setExecutor(this);

        this.cache = cache;
    }

    @Override
    public void execute(final Command cmd, final String alias, final String[] args) {
        if (args.length == 0) {
            System.out.println("User /pass «new password», please");
            return;
        }

        String newPass = args[0];

        if (newPass.equals(cache.getPassword())) {
            System.out.println("You've entered old password.");
            return;
        }

        cache.setPassword(newPass);
        System.out.printf("New password has been successfuly set.\n", newPass);
    }
}
