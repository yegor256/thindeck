/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
 * @author Yegor Bugayenko (yegor256@gmail.com)
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
