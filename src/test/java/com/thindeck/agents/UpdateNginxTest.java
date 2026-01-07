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
 * Test case for {@link UpdateNginx}.
 *
 * @since 0.5
 */
public final class UpdateNginxTest {

    /**
     * UpdateNginx can update nginx.
     * @throws IOException If fails
     */
    @Test
    public void updatesNginx() throws IOException {
        final Agent agent = new UpdateNginx(
            new Script.Fake("")
        );
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck name='test/test'><containers>",
                " <container type='green' state='alive'><http>80</http>",
                "  <host>localhost</host><name>aaaaaaaa</name>",
                "  <image>ffffffff</image>",
                " </container></containers>",
                "<domains><domain>test.thindeck.com</domain></domains></deck>"
            )
        );
        MatcherAssert.assertThat(
            "nginx was not updated correctly",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.node())
            ),
            XhtmlMatchers.hasXPaths("/deck")
        );
    }

}
