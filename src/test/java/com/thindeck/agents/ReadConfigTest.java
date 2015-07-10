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

import com.google.common.base.Joiner;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Deck;
import com.thindeck.mock.MkDeck;
import java.io.IOException;
import javax.json.Json;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
     * Fetch deck configuration and update deck accordingly.
     * @throws Exception If something goes wrong
     */
    @Test
    public void fetchesDeckConfigAndUpdatesDeck() throws Exception {
        final Deck deck = ReadConfigTest.deck();
        final Agent agent = new ReadConfig(ReadConfigTest.github());
        agent.exec(deck);
        MatcherAssert.assertThat(
            deck.read(),
            XhtmlMatchers.hasXPaths(
                "//deck/domains/domain[.='example.com']",
                "//deck/domains/domain[.='test.example.com']",
                "//deck/ports/port[.='80']",
                "//deck/ports/port[.='443']"
            )
        );
    }

    /**
     * Run exec twice and ensure that values are not duplicated.
     * @throws Exception If something goes wrong
     */
    @Test
    public void multipleExecDoesNotDuplicate() throws Exception {
        final Deck deck = ReadConfigTest.deck();
        final Agent agent = new ReadConfig(ReadConfigTest.github());
        agent.exec(deck);
        agent.exec(deck);
        MatcherAssert.assertThat(
            deck.read().nodes("//deck/ports/port").size(),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            deck.read().nodes("//deck/domains/domain").size(),
            Matchers.equalTo(2)
        );
    }

    /**
     * Create a context for tests.
     * @return Context
     * @throws IOException If something goes wrong
     */
    private static Deck deck() throws IOException {
        final Deck deck = new MkDeck();
        deck.update(
            new Directives()
                .xpath("/deck")
                .add("uri").set("git://github.com/thindeck/test.git")
        );
        return deck;
    }

    /**
     * Create Github and a Deck for tests.
     * @return Github
     * @throws IOException If something goes wrong
     */
    private static Github github() throws IOException {
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
        return ghub;
    }
}
