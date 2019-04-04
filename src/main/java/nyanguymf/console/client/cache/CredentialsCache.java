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
package nyanguymf.console.client.cache;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;

import nyanguymf.console.client.io.ClientInputManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class CredentialsCache {
    private static byte tries;

    private Scanner sc;
    private String login, password, host;
    private int port = -1;

    public CredentialsCache(final Scanner in) {
        sc = in;
    }

    public CredentialsCache initialize() {
        ClientInputManager.setIgnoring(true);

        System.out.print("Enter login%s: ");
        final String newLogin = sc.nextLine();
        login = newLogin.equals("") ? login : newLogin;

        System.out.print("Enter password: ");
        final String newPassword = sc.nextLine();
        password = newPassword.equals("") ? password : newPassword;

        System.out.print("Enter host: ");
        final String newHost = sc.nextLine();
        password = newHost.equals("") ? host : newHost;

        System.out.print("Enter port: ");
        port = parsePortRecursively();

        ClientInputManager.setIgnoring(false);

        return this;
    }

    private int parsePortRecursively() {
        int newPort = 0;

        try {
            newPort = parseInt(sc.nextLine());

            if ((newPort < 0) || (newPort > 65535))
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            // just skip it
            if (CredentialsCache.tries == 5) {
                System.out.println("Oh, come oooon, just type it right..");
            } else if (CredentialsCache.tries == 10) {
                System.out.println("Can't you just type number between 0 and 65535??");
            } else if (CredentialsCache.tries == 15) {
                System.out.println("Hey! Enter this fucking port right!");
            } else if (CredentialsCache.tries == 20) {
                System.out.println(
                    "Whot o fak, maaan, you've made "
                    + CredentialsCache.tries
                    + " tries to type port right, but it's still invalid"
                );
            } else if (CredentialsCache.tries >= 25) {
                System.out.println("Ok, just let me type it for you: ");
                try {
                    sleep(35L);
                    System.out.print(4);
                    sleep(25L);
                    System.out.print(4);
                    sleep(15L);
                    System.out.println("3...");
                } catch (InterruptedException ignore) {}
                return 443;
            }

            // the logic starts here
            System.out.print(
                "The port parameter is outside the specified range "
                + "of valid port values, which is between 0 and 65535,"
                + " inclusive. Enter again: "
            );
            ++CredentialsCache.tries;
            return parsePortRecursively();
        }

        if (CredentialsCache.tries >= 10 ) {
            System.out.println(
                "You took " + CredentialsCache.tries
                + " to type it.. Good job..."
            );
        }

        return newPort;
    }

    /** @return the login */
    public String getLogin() {
        return login;
    }

    /** @return the password */
    public String getPassword() {
        return password;
    }

    public String getPasswordHash() {
        return DigestUtils.md5Hex(password);
    }

    /** @return the host */
    public String getHost() {
        return host;
    }

    /** @return the port */
    public int getPort() {
        return port;
    }

    /** Sets login */
    public void setLogin(final String login) {
        this.login = login;
    }

    /** Sets password */
    public void setPassword(final String password) {
        this.password = password;
    }

    /** Sets host */
    public void setHost(final String host) {
        this.host = host;
    }

    /** Sets port */
    public void setPort(int port) {
        this.port = port;
    }
}
