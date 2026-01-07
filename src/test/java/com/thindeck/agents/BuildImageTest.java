/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.google.common.base.Joiner;
import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thindeck.api.Agent;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xembly.Xembler;

/**
 * Test case for {@link BuildImage}.
 *
 * @since 0.5
 */
final class BuildImageTest {

    /**
     * BuildImage can build image.
     * @throws IOException If fails
     */
    @Test
    void buildsImage() throws IOException {
        final Agent agent = new BuildImage(
            new Script.Fake("\n\n hey\nALIVE")
        );
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck name='test/test'>",
                "<repo><name>abcd1234</name><uri>http://</uri>",
                "</repo></deck>"
            )
        );
        MatcherAssert.assertThat(
            "image was not built correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.inner())
            ),
            XhtmlMatchers.hasXPaths(
                "/deck[not(repo)]",
                "/deck/images/image[@type='blue']"
            )
        );
    }

}
