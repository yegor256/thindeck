/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.thindeck.api.Decks;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Integration case for {@link DyBase}.
 *
 * @since 0.3
 */
final class DyBaseITCase {

    /**
     * DyBase can add a deck.
     * @throws Exception If there is some problem inside
     */
    @Test
    void canAddCommand() throws Exception {
        final Decks decks = new DyBase()
            .user("jeff")
            .decks();
        decks.add("booodeck");
        MatcherAssert.assertThat(
            "deck was not added correctly",
            decks.iterate().iterator().next().name(),
            Matchers.containsString("booo")
        );
    }

}
