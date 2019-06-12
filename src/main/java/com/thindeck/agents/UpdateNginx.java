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
 * "apt-get" or "yum" and that's it.</>
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
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
