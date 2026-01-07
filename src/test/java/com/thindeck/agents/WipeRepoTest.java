/**
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
 * Test case for {@link WipeRepo}.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class WipeRepoTest {

    /**
     * WipeRepo can wire a repo.
     * @throws IOException If fails
     */
    @Test
    public void wipesRepo() throws IOException {
        final Agent agent = new WipeRepo();
        final XML deck = new XMLDocument(
            Joiner.on(' ').join(
                "<deck name='test/test'><repo added='2013-01-01T12:59:59'>",
                "<name>abcdef59</name><uri>#</uri></repo></deck>"
            )
        );
        MatcherAssert.assertThat(
            new XMLDocument(
                new Xembler(agent.exec(deck)).applyQuietly(deck.node())
            ),
            XhtmlMatchers.hasXPaths("/deck[not(repo)]")
        );
    }

}
