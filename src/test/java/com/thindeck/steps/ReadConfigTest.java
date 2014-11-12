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

import com.google.common.base.Joiner;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Context;
import com.thindeck.api.Step;
import com.thindeck.api.mock.MkContext;
import javax.json.Json;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link ReadConfig}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class ReadConfigTest {

    /**
     * Fetch repo configuration and update memo accordingly.
     * @throws Exception If something goes wrong
     * @todo Unit test for #369 should rather be extracted
     */
    @Test
    public void fetchesRepoConfigAndUpdatesMemo() throws Exception {
        final Github ghub = new MkGithub("thindeck");
        final Repo repo = ghub.repos().create(
            Json.createObjectBuilder()
                .add("name", "test")
                .build()
        );
        final String config = Joiner.on('\n').join(
            "domains: [\"example.com\", \"test.example.com\"]",
            "ports: [80, 443]"
        );
        repo.contents().create(
            Json.createObjectBuilder()
                .add("path", ".thindeck.yml")
                .add("message", "Thindeck config")
                .add(
                    "content",
                    new String(
                        Base64.encodeBase64(
                            config.getBytes()
                        )
                    )
                ).build()
        );
        final Context ctx = new MkContext();
        ctx.memo().update(
            new Directives()
                .xpath("/memo")
                .add("uri").set("git://github.com/thindeck/test.git")
        );
        final Step step = new ReadConfig(ghub);
        step.exec(ctx);
        step.exec(ctx);
        MatcherAssert.assertThat(
            ctx.memo().read(),
            XhtmlMatchers.hasXPaths(
                "//memo/domains/domain[.='example.com']",
                "//memo/domains/domain[.='test.example.com']",
                "//memo/ports/port[.='80']",
                "//memo/ports/port[.='443']"
            )
        );
        Assert.assertEquals(
            2, ctx.memo().read().nodes("//memo/ports/port").size()
        );
        Assert.assertEquals(
            2, ctx.memo().read().nodes("//memo/domains/domain").size()
        );
    }
}
