/**
 * Copyright (c) 2015, Thindeck.com
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

import com.thindeck.api.Context;
import com.thindeck.api.Step;
import com.thindeck.api.mock.MkContext;
import java.io.IOException;
import org.junit.Test;
import org.mockito.Mockito;
import org.xembly.Directives;

/**
 * Test case for {@link UpdateLB}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class UpdateLBTest {

    /**
     * FindTanks can update load balancer configuration from memo.
     * @throws IOException If fails
     */
    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void updatesLoadBalancerConfig() throws IOException {
        final LoadBalancer balancer = Mockito.mock(LoadBalancer.class);
        final Step step = new UpdateLB(balancer);
        final Context ctx = new MkContext();
        // @checkstyle MagicNumber (6 lines)
        final String domain = "www.example.com";
        final int firstport = 80;
        final int secondport = 443;
        final String tank = "tank.thindeck.com";
        final int firstout = 32667;
        final int secondout = 32668;
        // @checkstyle MultipleStringLiterals (30 lines)
        ctx.memo().update(
            new Directives()
                .xpath("/memo")
                .addIf("domains")
                .addIf("domain").set(domain).up().up()
                .addIf("ports")
                .add("port").set(String.valueOf(firstport)).up()
                .add("port").set(String.valueOf(secondport)).up()
                .up()
                .addIf("containers")
                .addIf("container").attr("type", "green")
                 // @checkstyle LineLength (1 line)
                .add("cid").set("abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789").up()
                .add("ports")
                .add("port")
                .add("in").set(String.valueOf(firstport)).up()
                .add("out").set(String.valueOf(firstout)).up()
                .up()
                .add("port")
                .add("in").set(String.valueOf(secondport)).up()
                .add("out").set(String.valueOf(secondout)).up()
                .up().up()
                .add("dir").set("/fake/dir").up()
                .add("tank").set(tank)
        );
        step.exec(ctx);
        Mockito.verify(balancer).update(domain, firstport, tank, firstout);
        Mockito.verify(balancer).update(domain, secondport, tank, secondout);
    }

}
