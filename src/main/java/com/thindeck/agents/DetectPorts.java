/*
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
