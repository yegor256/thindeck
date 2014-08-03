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

/**
 * A deployed and working instance of Repository.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #256 Add handling of changing number of containers in runtime.
 * @todo #256 Add stopping of application and it's underlying container, server
 *  and cloud.
 */
public final class Deployment {

    /**
     * Containers used by this deployment.
     */
    private final transient Collection<Container> containers;

    /**
     * Constructor.
     * @param cntinrs Container to use.
     */
    public Deployment(final Collection<Container> cntinrs) {
        this.containers = cntinrs;
    }

    /**
     * Create instance of application.
     * @return Application created.
     * @todo #256 Add ability to create application in multiple containers.
     */
    public Application start() {
        final Container container = new Cloud().server().container();
        final Application app = container.application();
        this.containers.add(container);
        return app;
    }
}

