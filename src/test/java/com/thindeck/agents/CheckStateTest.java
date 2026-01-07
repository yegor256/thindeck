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
 * Test case for {@link CheckState}.
 *
 * @since 0.5
 */
public final class CheckStateTest {

    /**
     * CheckState can check state.
     * @throws IOException If fails
     */
    @Test
    public void checksState() throws IOException {
        final Agent agent = new CheckState(
            new Script.Fake("\n\n hey\nALIVE")
        );
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck><containers><container state='unknown'>",
                "<name>abcd1234</name><host>127.0.0.1</host>",
                "<http>80</http>",
                "</container></containers></deck>"
            )
        );
        MatcherAssert.assertThat(
            "state was not checked correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.node())
            ),
            XhtmlMatchers.hasXPaths(
                "/deck/containers/container[@state='alive']"
            )
        );
    }

}
