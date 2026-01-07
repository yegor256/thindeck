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
 * Stop all waste containers.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class StopDocker implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public StopDocker() throws IOException {
        this(
            new Script.Default(
                StopDocker.class.getResource("stop-docker.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public StopDocker(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> containers = deck.nodes(
            "/deck/containers/container[@waste and @state='dead']"
        );
        final Directives dirs = new Directives();
        for (final XML ctr : containers) {
            final String name = ctr.xpath("name/text()").get(0);
            Logger.info(
                this, "Docker container %s has to stop, it's waste",
                name
            );
            this.stop(
                ctr.xpath("host/text()").get(0),
                name
            );
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    name
                )
            ).remove();
        }
        return dirs;
    }

    /**
     * Stop docker container.
     * @param host Host
     * @param name Name of container
     * @throws IOException If fails
     */
    private void stop(final String host, final String name)
        throws IOException {
        this.script.exec(
            host,
            new ArrayMap<String, String>().with("container", name)
        );
        Logger.info(
            StartDocker.class,
            "Docker container %s stopped at %s", name, host
        );
    }

}
