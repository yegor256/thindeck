/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.fakes.FkBase;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rs.RsPrettyXML;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkIndex}.
 * @since 0.4
 */
public final class TkIndexTest {

    /**
     * TkIndex can render a page in XML.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void rendersXmlPage() throws Exception {
        MatcherAssert.assertThat(
            "XML page was not rendered correctly",
            new RsPrint(
                new RsPrettyXML(
                    new TkIndex(new FkBase()).act(
                        new RqWithHeader(
                            new RqFake("GET", "/"),
                            "Accept",
                            "text/xml"
                        )
                    )
                )
            ).printBody(),
            XhtmlMatchers.hasXPaths(
                "/page[@date]",
                "/page/links/link[@rel='home']"
            )
        );
    }
}
