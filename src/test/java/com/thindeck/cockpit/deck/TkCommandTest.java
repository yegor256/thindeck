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
package com.thindeck.cockpit.deck;

import com.google.common.base.Joiner;
import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import com.thindeck.fakes.FkBase;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Request;
import org.takes.facets.auth.RqWithAuth;
import org.takes.facets.fork.TkFork;
import org.takes.facets.forward.TkForward;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkCommand}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkCommandTest {

    /**
     * TkCommand can forward to the right URL.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void forwardsCorrectly() throws Exception {
        final String name = "orchard";
        final String urn = "urn:test:554";
        final Request req = new RqWithAuth(
            urn,
            new RqFake(
                "X",
                String.format(
                    "/d/%s?command=domain+remove+x.com", name
                )
            )
        );
        final Base base = new FkBase();
        final Decks decks = base.user(urn).decks();
        decks.add(name);
        MatcherAssert.assertThat(
            new RsPrint(
                new TkForward(
                    new TkFork(
                        new FkDeck("", new TkCommand(base))
                    )
                ).act(req)
            ).printHead(),
            Matchers.containsString(
                String.format("Location: http://www.example.com/d/%s", name)
            )
        );
    }

    /**
     * TkCommand can add new domain.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void addsNewDomain() throws Exception {
        final String name = "foo";
        final String urn = "urn:test:19";
        final Request req = new RqWithAuth(
            urn,
            new RqFake(
                "GET",
                String.format(
                    "/d/%s?command=domain+add+test.thindeck.com", name
                )
            )
        );
        final Base base = new FkBase();
        final Decks decks = base.user(urn).decks();
        decks.add(name);
        new FkDeck("", new TkForward(new TkCommand(base))).route(req).get();
        MatcherAssert.assertThat(
            new Deck.Smart(decks.get(name)).xml(),
            XhtmlMatchers.hasXPaths(
                "/deck/domains[count(domain)=1]",
                "/deck/domains[domain='test.thindeck.com']"
            )
        );
    }

    /**
     * TkCommand can add new repo.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void addsNewRepo() throws Exception {
        final String name = "lion";
        final String urn = "urn:test:443";
        final Request req = new RqWithAuth(
            urn,
            new RqFake(
                "PUT",
                String.format(
                    "/d/%s?command=repo+put+https://github.com/yegor256/x.git",
                    name
                )
            )
        );
        final Base base = new FkBase();
        final Decks decks = base.user(urn).decks();
        decks.add(name);
        new FkDeck("", new TkForward(new TkCommand(base))).route(req).get();
        MatcherAssert.assertThat(
            new Deck.Smart(decks.get(name)).xml(),
            XhtmlMatchers.hasXPaths(
                "/deck/repo",
                "/deck/repo/uri"
            )
        );
    }

    /**
     * TkCommand can waste a container.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void wastesSelectedContainer() throws Exception {
        final String name = "tiger";
        final String urn = "urn:test:909";
        final Request req = new RqWithAuth(
            urn,
            new RqFake(
                "POST",
                String.format("/d/%s?command=container+waste+a1b2c3e4", name)
            )
        );
        final Base base = new FkBase();
        final Decks decks = base.user(urn).decks();
        decks.add(name);
        new Deck.Smart(decks.get(name)).update(
            Joiner.on(' ').join(
                "<deck><containers><container",
                " type='blue' state='alive'><name>a1b2c3e4</name>",
                "<host>127.0.0.1</host><image>foo/foo-aaaabbbb</image>",
                "</container></containers></deck>"
            )
        );
        new FkDeck("", new TkForward(new TkCommand(base))).route(req).get();
        MatcherAssert.assertThat(
            new Deck.Smart(decks.get(name)).xml(),
            XhtmlMatchers.hasXPaths(
                "/deck/containers/container[name='a1b2c3e4' and @waste]"
            )
        );
    }

    /**
     * TkCommand can waste an image.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void wastesSelectedImage() throws Exception {
        final String name = "panda";
        final String urn = "urn:test:4333";
        final Request req = new RqWithAuth(
            urn,
            new RqFake(
                "INFO",
                String.format(
                    "/d/%s?command=image+waste+test/test-a1b2c3e4", name
                )
            )
        );
        final Base base = new FkBase();
        final Decks decks = base.user(urn).decks();
        decks.add(name);
        new Deck.Smart(decks.get(name)).update(
            Joiner.on(' ').join(
                "<deck><images><image",
                " type='blue'><name>test/test-a1b2c3e4</name>",
                "<repo>abcd0099</repo><uri>#</uri>",
                "</image></images></deck>"
            )
        );
        new FkDeck("", new TkForward(new TkCommand(base))).route(req).get();
        MatcherAssert.assertThat(
            new Deck.Smart(decks.get(name)).xml(),
            XhtmlMatchers.hasXPaths(
                "/deck/images/image[name='test/test-a1b2c3e4' and @waste]"
            )
        );
    }
}
