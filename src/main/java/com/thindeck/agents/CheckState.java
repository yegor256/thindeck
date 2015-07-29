/**
 * Copyright (c) 2014-2015, Thindeck.com
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
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.util.Collection;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Check state of Docker containers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class CheckState implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public CheckState() throws IOException {
        this(
            new Script.Default(
                CheckState.class.getResource("check-state.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public CheckState(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Directives dirs = new Directives();
        final Collection<XML> containers = deck.nodes(
            "/deck/containers/container[host and http]"
        );
        final String today = new Today().iso();
        for (final XML ctr : containers) {
            final Integer port = Integer.parseInt(
                ctr.xpath("http/text()").get(0)
            );
            final String host = ctr.xpath("host/text()").get(0);
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    ctr.xpath("name/text()").get(0)
                )
            );
            final String before = ctr.xpath("@state").get(0);
            final String after = this.state(host, port);
            if (!after.equals(before)) {
                dirs.attr("state", after).attr("checked", today);
                Logger.info(
                    this, "State of container %s changed from %s to %s",
                    ctr.xpath("name/text()").get(0), before, after
                );
            }
        }
        return dirs;
    }

    /**
     * Get state.
     * @param host Host name of the tank
     * @param port Port to check (HTTP)
     * @return State
     * @throws IOException If fails
     */
    private String state(final String host, final int port) throws IOException {
        final String stdout = this.script.exec(
            host,
            new ArrayMap<String, String>().with("port", Integer.toString(port))
        );
        final String state;
        if (stdout.contains("DEAD")) {
            state = "dead";
            Logger.info(
                this, "HTTP port %d at %s doesn't respond",
                port, host
            );
        } else {
            state = "alive";
        }
        return state;
    }

}
