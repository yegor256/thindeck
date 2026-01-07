/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import com.thindeck.api.Deck;
import java.io.IOException;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Test case for {@link FkDeck}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class FkDeckTest {

    /**
     * FkDeck can accept info about domain configuration.
     * @throws IOException If an IO error gets thrown
     */
    @Test
    public void acceptsDomainDefinition() throws IOException {
        final Deck deck = new FkDeck();
        deck.exec(
            new Agent() {
                @Override
                public Iterable<Directive> exec(final XML xml) {
                    return new Directives().xpath("/deck")
                        .attr("name", "test/test");
                }
            }
        );
        deck.exec(
            new Agent() {
                @Override
                public Iterable<Directive> exec(final XML xml) {
                    MatcherAssert.assertThat(
                        xml,
                        XhtmlMatchers.hasXPaths("/deck[@name='test/test']")
                    );
                    return Collections.emptyList();
                }
            }
        );
    }

}
