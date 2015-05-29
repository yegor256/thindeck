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
package com.thindeck.steps;

import com.jcabi.aspects.Tv;
import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Context;
import com.thindeck.api.Step;
import com.thindeck.api.mock.MkContext;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link Swap}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
public final class SwapTest {

    /**
     * Swap can swap containers.
     * @throws IOException If fails
     */
    @Test
    public void swapsGreenAndBlueContainers() throws IOException {
        final Step step = new Swap();
        final Context ctx = new MkContext();
        ctx.memo().update(
            new Directives().xpath("/memo").addIf("containers")
                .add("container").attr("type", "green")
                .add("cid")
                .set(StringUtils.repeat('a', 1 << Tv.SIX))
                .up()
                .add("dir").set("/tmp").up()
                .add("tank").set("localhost").up()
                .add("ports").add("port")
                .add("in").set("80").up().add("out").set("8090").up().up()
        );
        step.exec(ctx);
        MatcherAssert.assertThat(
            ctx.memo().read(),
            XhtmlMatchers.hasXPaths(
                "/memo/containers[count(container)=1]",
                "/memo/containers/container[@type='blue']"
            )
        );
    }

}
