/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.bosses;

import com.jcabi.immutable.ArrayMap;
import com.thindeck.agents.Script;
import com.thindeck.api.Boss;
import com.thindeck.api.Deck;
import java.io.IOException;

/**
 * Setup nginx load balancer.
 *
 * <p>To install nginx on a clean server, just install it first using
 * "apt-get" or "yum" and that's it.</>
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 */
public final class SetupNginx implements Boss {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public SetupNginx() throws IOException {
        this(
            new Script.Default(
                SetupNginx.class.getResource("setup-nginx.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public SetupNginx(final Script spt) {
        this.script = spt;
    }

    @Override
    public void exec(final Iterable<Deck> decks) throws IOException {
        this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>()
        );
    }

}
