/**
 * Copyright (c) 2014, thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thindeck;

import java.net.InetAddress;

/**
 * A physical (or virtual) machine provided on-demand by Cloud.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #256 Implement creation of container by the Server.
 */
public final class Server {

    /**
     * Server state.
     */
    private enum State { INITIALIZED, STARTING, RUNNING, TERMINATING }

    /**
     * IP address of the server.
     */
    private final transient InetAddress address;

    /**
     * Current state.
     */
    private final transient State state;

    /**
     * Constructor.
     * @param addrss IP address of the server.
     */
    public Server(final InetAddress addrss) {
        this.state = State.INITIALIZED;
        this.address = addrss;
    }

    /**
     * Retrieve IP address of the server.
     * @return Server IP address.
     */
    public InetAddress getAddress() {
        return this.address;
    }

    /**
     * Current server state.
     * @return State of the server.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Create container.
     * @return Container created.
     */
    public Container container() {
        return new Container(this);
    }
}
