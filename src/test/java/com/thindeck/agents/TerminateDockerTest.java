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
import org.junit.Test;
import org.xembly.Xembler;

/**
 * Test case for {@link TerminateDocker}.
 *
 * @since 0.1
 */
public final class TerminateDockerTest {

    /**
     * TerminateDocker can kill containers.
     * @throws IOException If fails
     */
    @Test
    public void killsContainers() throws IOException {
        final Agent agent = new TerminateDocker(
            new Script.Fake("\n\n")
        );
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck name='test/test'><containers>",
                " <container type='blue' state='alive'",
                " waste='2013-01-01T12:59:59'>",
                "  <host>localhost</host><name>aaaaaaaa</name>",
                "  <image>ffffffff</image>",
                " </container><container type='green'>",
                "  <name>bbbbbbbb</name>",
                "  <image>eeeeeeee</image>",
                " </container>",
                "</containers></deck>"
            )
        );
        MatcherAssert.assertThat(
            "containers were not terminated correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.node())
            ),
            XhtmlMatchers.hasXPaths(
                "/deck/containers[count(container)=2]"
            )
        );
    }

}
