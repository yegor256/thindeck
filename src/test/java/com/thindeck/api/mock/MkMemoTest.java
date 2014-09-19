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
package com.thindeck.api.mock;

import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Memo;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link MkMemo}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class MkMemoTest {

    /**
     * MkMemo can accept info about domain configuration.
     * @throws IOException If an IO error gets thrown
     */
    @Test
    public void acceptsDomainDefinition() throws IOException {
        final Memo ctx = new MkMemo();
        final String domain = "test.thindeck.com";
        final int first = 80;
        final int second = 8080;
        ctx.update(
            new Directives()
                .xpath("/memo")
                .addIf("domains")
                .addIf("domain").set(domain).up()
                .addIf("ports")
                // @checkstyle MultipleStringLiterals (2 lines)
                .add("port").set(String.valueOf(first)).up()
                .add("port").set(String.valueOf(second)).up()
        );
        MatcherAssert.assertThat(
            ctx.read(),
            XhtmlMatchers.hasXPaths(
                String.format("//memo/domains/domain[.='%s']", domain),
                String.format("//memo/domains/ports/port[.='%d']", first),
                String.format("//memo/domains/ports/port[.='%d']", second)
            )
        );
    }

}
