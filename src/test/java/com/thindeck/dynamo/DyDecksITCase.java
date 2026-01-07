/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link DyDecks}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
public final class DyDecksITCase {

    /**
     * DyDecks can add and remove a deck.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void canAddAndRemoveDeck() throws Exception {
        final Decks decks = new DyBase()
            .user("bobby")
            .decks();
        final String name = "test";
        decks.add(name);
        MatcherAssert.assertThat(
            decks.iterate(),
            Matchers.<Deck>iterableWithSize(1)
        );
        decks.delete(name);
        MatcherAssert.assertThat(
            decks.iterate(),
            Matchers.<Deck>emptyIterable()
        );
    }

}
