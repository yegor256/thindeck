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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Discover ports of Docker containers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class DetectPorts implements Agent {

    /**
     * Pattern to find all ports.
     */
    private static final Pattern PTN = Pattern.compile(
        "thindeck_([a-z]+)=(?:[\\d+\\.]+):(\\d+)",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public DetectPorts() throws IOException {
        this(
            new Script.Default(
                DetectPorts.class.getResource("docker-ports.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public DetectPorts(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Directives dirs = new Directives();
        final Collection<XML> containers = deck.nodes(
            // @checkstyle LineLength (1 line)
            "/deck/containers/container[not(@waste) and (not(http) or not(https))]"
        );
        for (final XML ctr : containers) {
            final String name = ctr.xpath("name/text()").get(0);
            final String host = ctr.xpath("host/text()").get(0);
            Logger.info(
                this, "Exposed ports of container %s at %s must be found",
                name, host
            );
            final Map<String, Integer> ports = this.ports(name, host);
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    name
                )
            );
            for (final Map.Entry<String, Integer> port : ports.entrySet()) {
                dirs.addIf(port.getKey())
                    .set(Integer.toString(port.getValue()))
                    .up();
            }
        }
        return dirs;
    }

    /**
     * Detect all ports.
     * @param name Docker container name
     * @param host Host name of the tank
     * @return Ports
     * @throws IOException If fails
     */
    private Map<String, Integer> ports(final String name, final String host)
        throws IOException {
        final ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>(0);
        final String stdout = this.script.exec(
            host,
            new ArrayMap<String, String>().with("container", name)
        );
        final Matcher matcher = DetectPorts.PTN.matcher(stdout);
        while (matcher.find()) {
            map.put(
                matcher.group(1),
                Integer.parseInt(matcher.group(2))
            );
        }
        Logger.info(
            this, "Docker container %s at %s exposes these ports: %s",
            name, host, map
        );
        return map;
    }

}
