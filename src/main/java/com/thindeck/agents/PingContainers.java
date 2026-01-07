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
 * Check docker container existence and remove if absent.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class PingContainers implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public PingContainers() throws IOException {
        this(
            new Script.Default(
                PingContainers.class.getResource("ping-container.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public PingContainers(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> containers = deck.nodes(
            "/deck/containers/container"
        );
        final Directives dirs = new Directives();
        for (final XML ctr : containers) {
            final String name = ctr.xpath("name/text()").get(0);
            final String tank = ctr.xpath("host/text()").get(0);
            if (!this.exists(name, tank)) {
                dirs.xpath(
                    String.format("/deck/containers/container[name='%s']", name)
                ).remove();
            }
        }
        return dirs;
    }

    /**
     * Check docker container existence.
     * @param name Docker container name
     * @param host Host name of the tank
     * @return TRUE if exists
     * @throws IOException If fails
     */
    private boolean exists(final String name, final String host)
        throws IOException {
        final String stdout = this.script.exec(
            host,
            new ArrayMap<String, String>().with("container", name)
        );
        final boolean exists = !stdout.contains("DEAD");
        if (!exists) {
            Logger.error(
                this, "Docker container %s is absent at %s",
                name, host
            );
        }
        return exists;
    }

}
