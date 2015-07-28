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
 * @author Yegor Bugayenko (yegor@teamed.io)
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
     */
    public TerminateDocker() {
        this(new Script.Default("terminate-docker.sh"));
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
