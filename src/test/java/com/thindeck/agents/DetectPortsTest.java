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
 * Test case for {@link DetectPorts}.
 *
 * @since 0.5
 */
final class DetectPortsTest {

    /**
     * DetectPorts can detect ports.
     * @throws IOException If fails
     */
    @Test
    void detectsPorts() throws IOException {
        final Agent agent = new DetectPorts(
            new Script.Fake(
                Joiner.on('\n').join(
                    "\n\n hey\nthindeck_http=0.0.0.0:8080",
                    "something else here",
                    "thindeck_https=0.0.0.0:45600"
                )
            )
        );
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck><containers><container>",
                "<name>abcd1234</name><host>127.0.0.1</host>",
                "</container></containers></deck>"
            )
        );
        MatcherAssert.assertThat(
            "ports were not detected correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.inner())
            ),
            XhtmlMatchers.hasXPaths(
                "/deck/containers/container[http=8080 and https=45600]"
            )
        );
    }

}
