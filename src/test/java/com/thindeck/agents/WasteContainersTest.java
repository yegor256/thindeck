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
 * Test case for {@link WasteContainers}.
 *
 * @since 0.1
 */
final class WasteContainersTest {

    /**
     * WasteContainers can waste containers.
     * @throws IOException If fails
     */
    @Test
    void wastesContainers() throws IOException {
        final Agent agent = new WasteContainers();
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck name='test/test'><containers>",
                " <container type='blue'>",
                "  <http>8080</http><name>aaaaaaaa</name>",
                "  <image>ffffffff</image>",
                " </container><container type='green'>",
                "  <name>bbbbbbbb</name>",
                "  <image>eeeeeeee</image>",
                " </container>",
                "</containers></deck>"
            )
        );
        MatcherAssert.assertThat(
            "containers were not wasted correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.inner())
            ),
            XhtmlMatchers.hasXPaths(
                "/deck/containers[count(container)=2]",
                "//container[name='aaaaaaaa' and not(@waste)]",
                "//container[name='bbbbbbbb' and @waste]"
            )
        );
    }

}
