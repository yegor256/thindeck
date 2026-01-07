/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thindeck.api.Agent;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xembly.Xembler;

/**
 * Test case for {@link FindTanks}.
 *
 * @since 0.1
 */
final class FindTanksTest {

    /**
     * FindTanks can find tanks and report them in XML.
     * @throws IOException If fails
     */
    @Test
    void findsTanksAndDocumentsInXml() throws IOException {
        final Agent agent = new FindTanks();
        final XML deck = new XMLDocument("<deck/>");
        MatcherAssert.assertThat(
            "tanks were not found",
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.inner())
            ),
            XhtmlMatchers.hasXPaths("/deck/tanks/tank")
        );
    }

}
