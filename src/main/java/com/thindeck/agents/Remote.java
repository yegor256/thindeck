/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Remote shell(s).
 *
 * @since 0.1
 */
@Immutable
public final class Remote implements Shell {

    /**
     * Host.
     */
    private final transient String host;

    /**
     * Ctor.
     * @param hst Host
     */
    public Remote(final String hst) {
        this.host = hst;
    }

    @Override
    // @checkstyle ParameterNumberCheck (3 lines)
    public int exec(final String command, final InputStream stdin,
        final OutputStream stdout, final OutputStream stderr)
        throws IOException {
        return new Shell.Verbose(
            new Shell.Safe(
                new SSH(
                    // @checkstyle MagicNumber (1 line)
                    this.host, 22, "thindeck",
                    IOUtils.toString(
                        this.getClass().getResourceAsStream("thindeck.key")
                    )
                )
            )
        ).exec(command, stdin, stdout, stderr);
    }
}
