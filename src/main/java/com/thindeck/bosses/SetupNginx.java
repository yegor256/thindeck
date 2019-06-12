/**
 * Copyright (c) 2014-2019, Thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the thindeck.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
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
