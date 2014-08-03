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
import java.net.UnknownHostException;

/**
 * A provider of on-demand virtual or physical servers.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #256 Implement creation of servers by the Cloud.
 * @todo #256 Add logging to the cloud provider and remove PMD.SingularField
 *  and PMD.UnusedPrivateField.
 */
@SuppressWarnings({ "PMD.SingularField", "PMD.UnusedPrivateField" })
public final class Cloud {
    /**
     * Username for Cloud access.
     */
    private final transient String username;

    /**
     * Password for Cloud access.
     */
    private final transient String password;

    /**
     * Constructor.
     */
    public Cloud() {
        this.username = "";
        this.password = "";
    }

    /**
     * Creates server.
     * @todo #256 Implement correct server creation in the Cloud.
     * @return Server created.
     */
    public Server server() {
        try {
            return new Server(InetAddress.getLocalHost());
        } catch (final UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
