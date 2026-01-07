/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Base;
import com.thindeck.fakes.FkBase;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.takes.facets.auth.RqWithAuth;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rs.RsPrettyXML;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkDecks}.
 * @since 0.4
 */
public final class TkDecksTest {

    /**
     * TkDecks can render a page in XML.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void rendersXmlPage() throws Exception {
        final Base base = new FkBase();
        final String urn = "urn:test:199";
        base.user(urn).decks().add("elephant");
        MatcherAssert.assertThat(
            "decks page was not rendered correctly",
            new RsPrint(
                new RsPrettyXML(
                    new TkDecks(base).act(
                        new RqWithAuth(
                            urn,
                            new RqWithHeader(
                                new RqFake("GET", "/"),
                                "Accept",
                                "text/xml"
                            )
                        )
                    )
                )
            ).printBody(),
            XhtmlMatchers.hasXPaths(
                "/page/links/link[@rel='home']",
                "/page/items[count(item)=1]",
                "//deck[@name='test/elephant']",
                "//item/links/link[@rel='open']"
            )
        );
    }

}
