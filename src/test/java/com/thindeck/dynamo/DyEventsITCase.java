/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.jcabi.log.Logger;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.Events;
import com.thindeck.api.User;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Integration case for {@link DyEvents}.
 * @since 0.5
 */
public final class DyEventsITCase {

    /**
     * DyEvents can add an event.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void addsEvent() throws Exception {
        final Base base = new DyBase();
        final User user = base.user("sarah");
        user.decks().add("foo");
        final Deck deck = user.decks().iterate().iterator().next();
        final Events events = deck.events();
        Logger.info(this, "some log line, for tests");
        events.create("first one");
        MatcherAssert.assertThat(
            "event was not added correctly",
            events.iterate(Long.MAX_VALUE).iterator().next(),
            Matchers.startsWith("first ")
        );
    }

}
