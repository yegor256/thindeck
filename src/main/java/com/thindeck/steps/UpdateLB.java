/**
 * Copyright (c) 2014, Thindeck.com
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
package com.thindeck.steps;

import com.jcabi.aspects.Immutable;
import com.jcabi.xml.XML;
import com.thindeck.api.Context;
import com.thindeck.api.Step;
import java.io.IOException;
import java.util.List;

/**
 * Update Load Balancer.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.3
 */
@Immutable
public final class UpdateLB implements Step {

    /**
     * Load Balancer to use.
     */
    private final transient LoadBalancer balancer;

    /**
     * Public ctor.
     * @param bal The load balancer
     */
    public UpdateLB(final LoadBalancer bal) {
        this.balancer = bal;
    }

    @Override
    public String name() {
        return "update-lb";
    }

    @Override
    public void exec(final Context ctx) throws IOException {
        final XML memo = ctx.memo().read();
        final List<String> domains = memo.xpath("/memo/domains/domain/text()");
        final List<String> ports = memo.xpath("/memo/ports/port/text()");
        final List<XML> containers = memo.nodes("/memo/containers/container");
        for (final XML container : containers) {
            final String tank = container
                .xpath("/container/tank/text()").get(0);
            for (final String port : ports) {
                final List<String> outports = container.xpath(
                    String.format(
                        "/container/ports/port[in/text()='%s']/out/text()",
                        port
                    )
                );
                this.updates(domains, tank, port, outports);
            }
        }
    }

    @Override
    public void commit(final Context ctx) throws IOException {
        // nothing to commit
    }

    @Override
    public void rollback(final Context ctx) throws IOException {
        // nothing to rollback
    }

    /**
     * Perform LB updates for given list of domains, containers and ports.
     * @param domains Domains to balance.
     * @param tank Container to use.
     * @param port Input port.
     * @param outports Output ports.
     * @checkstyle ParameterNumber (3 lines)
     */
    private void updates(final List<String> domains, final String tank,
        final String port, final List<String> outports) {
        for (final String domain : domains) {
            for (final String outport : outports) {
                this.balancer.update(
                    domain, Integer.parseInt(port),
                    tank, Integer.parseInt(outport)
                );
            }
        }
    }
}
