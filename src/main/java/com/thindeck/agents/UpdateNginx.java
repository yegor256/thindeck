/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Update nginx load balancer.
 *
 * <p>Assumption is that ngnix configuration loaded from {@code ngnix.conf}
 * and each host has a conf file named {@code www.example.com.conf},
 * which will contain load balancing group, e.g:
 *
 * <pre> upstream example_servers {
 *   server 10.0.0.1:80;
 *   server 10.0.0.2:80;
 * }
 * server {
 *   listen 80;
 *   server_name www.example.com;
 *   location / {
 *     proxy_pass http://example_servers;
 *   }
 * }</pre>
 *
 * <p>This file is loaded from main {@code ngnix.conf}
 * using {@code include} directive.
 *
 * <p>To install nginx on a clean server, just install it first using
 * "apt-get" or "yum" and that's it.
 *
 * @since 0.5
 * @checkstyle MultipleStringLiterals (300 lines)
 */
public final class UpdateNginx implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public UpdateNginx() throws IOException {
        this(
            new Script.Default(
                UpdateNginx.class.getResource("update-nginx.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public UpdateNginx(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        for (final String domain : deck.xpath("/deck/domains/domain/text()")) {
            this.update(domain, deck);
        }
        return new Directives();
    }

    /**
     * Update for one domain.
     * @param domain Domain name
     * @param deck The deck
     * @throws IOException If fails
     */
    private void update(final String domain, final XML deck)
        throws IOException {
        // @checkstyle LineLength (1 line)
        final String terms = "not(@waste) and @type='green' and @state='alive' and http";
        final String servers = Joiner.on(' ').join(
            Iterables.transform(
                deck.nodes(String.format("//container[%s]", terms)),
                new Function<XML, String>() {
                    @Override
                    public String apply(final XML ctr) {
                        return String.format(
                            "server %s:%s; ",
                            ctr.xpath("host/text()").get(0),
                            ctr.xpath("http/text()").get(0)
                        );
                    }
                }
            )
        );
        this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>()
                .with("deck", deck.xpath("/deck/@name").get(0))
                .with(
                    "images",
                    Joiner.on(',').join(
                        deck.xpath(
                            String.format("//container[%s]/image/text()", terms)
                        )
                    )
                )
                .with("port", "80")
                .with("group", domain.replace(".", "_"))
                .with("domain", domain)
                .with("servers", servers)
        );
    }

}
