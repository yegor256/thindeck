/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.User;
import com.thindeck.cockpit.RqUser;
import java.io.IOException;
import java.net.HttpURLConnection;
import lombok.EqualsAndHashCode;
import org.takes.Request;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqHeaders;
import org.takes.rq.RqWrap;
import org.takes.rs.RsText;

/**
 * Deck fork.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
@EqualsAndHashCode(callSuper = true)
public final class RqDeck extends RqWrap {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse The base
     * @param req Request
     */
    public RqDeck(final Base bse, final Request req) {
        super(req);
        this.base = bse;
    }

    /**
     * Get deck.
     * @return The deck
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    public Deck deck() throws IOException {
        final User user = new RqUser(this, this.base).get();
        final String name = new RqHeaders.Smart(
            new RqHeaders.Base(this)
        ).single("X-Thindeck-Deck");
        try {
            return user.decks().get(name);
        } catch (final IOException ex) {
            throw new RsForward(
                new RsText(ex.getLocalizedMessage()),
                HttpURLConnection.HTTP_NOT_FOUND
            );
        }
    }

}
