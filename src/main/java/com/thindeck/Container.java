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

import java.util.Collection;
import java.util.LinkedList;

/**
 * A virtual operating system running user's applications build from Repository.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #256 Implement Invoice class and add it to Container.
 * @todo #256 Implement better way of storing logs, right now collection will
 *  eat a lot of RAM if the Container is running for longer periods of time,
 *  and remove  PMD.SingularField and PMD.UnusedPrivateField
 */
@SuppressWarnings({ "PMD.SingularField", "PMD.UnusedPrivateField" })
public final class Container {
    /**
     * Container state.
     */
    private enum State { INITIALIZED, BUILDING, RUNNING }

    /**
     * This containers server.
     */
    private final transient Server server;

    /**
     * Logs.
     */
    private final transient Collection<String> logs;

    /**
     * Current state.
     */
    private final transient State state;

    /**
     * Application currently running in container.
     */
    private transient Application app;

    /**
     * Constructor.
     * @param srvr Server on which the container is placed.
     */
    public Container(final Server srvr) {
        this.state = State.INITIALIZED;
        this.logs = new LinkedList<String>();
        this.server = srvr;
    }

    /**
     * Current container state.
     * @return Container state.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Server in which the container is run.
     * @return Server used by container.
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Create application.
     * @return Created application.
     */
    public Application application() {
        return new Application();
    }
}
