/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.thindeck.api.Decks;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link DyBase}.
 * @author Paul Polishchuk (ppol@ua.fm)
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 */
public final class DyBaseITCase {

    /**
     * DyBase can add a deck.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void canAddCommand() throws Exception {
        final Decks decks = new DyBase()
            .user("jeff")
            .decks();
        decks.add("booodeck");
        MatcherAssert.assertThat(
            decks.iterate().iterator().next().name(),
            Matchers.containsString("booo")
        );
    }

}
