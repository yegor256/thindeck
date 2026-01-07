/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.fakes.FkBase;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.takes.facets.auth.RqWithAuth;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkApp}.
 * @since 0.4
 */
public final class TkAppTest {

    /**
     * TkApp can render home page.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void rendersHomePage() throws Exception {
        MatcherAssert.assertThat(
            "home page was not rendered correctly",
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(new FkBase()).act(
                        new RqWithAuth(
                            "urn:test:1",
                            new RqWithHeader(
                                new RqFake("GET", "/"),
                                "Accept",
                                "text/xml"
                            )
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths(
                "/page[@date]",
                "/page/links/link[@rel='home']"
            )
        );
    }
}
