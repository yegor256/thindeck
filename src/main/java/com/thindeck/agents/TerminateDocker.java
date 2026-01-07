/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Terminate waste containers that is ALIVE for too long.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class TerminateDocker implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public TerminateDocker() throws IOException {
        this(
            new Script.Default(
                TerminateDocker.class.getResource("terminate-docker.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public TerminateDocker(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> containers = deck.nodes(
            "/deck/containers/container[@waste and @state='alive']"
        );
        final Date today = new Date();
        for (final XML ctr : containers) {
            final int age;
            try {
                age = (int) ((today.getTime()
                    - DateFormatUtils.ISO_DATETIME_FORMAT.parse(
                        ctr.xpath("@waste").get(0)
                    ).getTime()) / TimeUnit.MINUTES.toMillis(1L));
            } catch (final ParseException ex) {
                throw new IOException(ex);
            }
            if (age < Tv.TEN) {
                continue;
            }
            final String name = ctr.xpath("name/text()").get(0);
            Logger.info(
                this, "Container %s has to die, it's in waste for over %d mins",
                name, age
            );
            this.terminate(
                ctr.xpath("host/text()").get(0),
                name
            );
        }
        return new Directives();
    }

    /**
     * Terminate docker container.
     * @param host Host
     * @param name Name of container
     * @throws IOException If fails
     */
    private void terminate(final String host, final String name)
        throws IOException {
        this.script.exec(
            host,
            new ArrayMap<String, String>().with("container", name)
        );
        Logger.info(
            StartDocker.class,
            "Docker container %s terminated at %s", name, host
        );
    }

}
