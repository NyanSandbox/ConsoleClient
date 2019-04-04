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
package nyanguymf.console.client.net;

import static nyanguymf.console.client.net.ConnectionStatus.CONNECTION_REFUSED;
import static nyanguymf.console.client.net.ConnectionStatus.INVALID_PORT;
import static nyanguymf.console.client.net.ConnectionStatus.UNKNOWN_HOST;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import nyanguymf.console.client.cache.CredentialsCache;
import nyanguymf.console.client.io.ServerInputManager;
import nyanguymf.console.client.io.ServerOutputManager;

/** @author NyanGuyMF - Vasiliy Bely */
public final class ConnectionManager {
    private SSLSocketFactory factory =
            (SSLSocketFactory)SSLSocketFactory.getDefault();
    private ServerOutputManager out;
    private ServerInputManager in;
    private ConnectionStatus status;
    private SSLSocket socket;

    public ConnectionManager connect(final CredentialsCache cache) throws Exception {
        try {
            socket = (SSLSocket)factory.createSocket(cache.getHost(), cache.getPort());

            /*
             * send http request
             *
             * Before any application data is sent or received, the
             * SSL socket will do SSL handshaking first to set up
             * the security attributes.
             *
             * SSL handshaking can be initiated by either flushing data
             * down the pipe, or by starting the handshaking by hand.
             *
             * Handshaking is started manually in this example because
             * PrintWriter catches all IOExceptions (including
             * SSLExceptions), sets an internal error flag, and then
             * returns without rethrowing the exception.
             *
             * Unfortunately, this means any error messages are lost,
             * which caused lots of confusion for others using this
             * code.  The only way to tell there was an error is to call
             * PrintWriter.checkError().
             */
            socket.startHandshake();

            out = new ServerOutputManager(socket.getOutputStream());
            in = new ServerInputManager(socket.getInputStream(), cache);

            in.start();

        } catch (IllegalArgumentException ex) {
            status = INVALID_PORT;
        } catch (UnknownHostException ex) {
            status = UNKNOWN_HOST;
        } catch (ConnectException ex) {
            status = CONNECTION_REFUSED;
        }

        return this;
    }

    /** @return the status */
    public ConnectionStatus getStatus() {
        return status;
    }

    public void close() {
        out.close();
        in.close();
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** @return the in */
    public ServerInputManager getIn() {
        return in;
    }

    /** @return the out */
    public ServerOutputManager getOut() {
        return out;
    }
}
