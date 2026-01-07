/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import com.thindeck.api.Base;
import com.thindeck.api.Decks;
import com.thindeck.cockpit.RqUser;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;

/**
 * Delete deck.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkDelete implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkDelete(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final Decks decks = new RqUser(req, this.base).get().decks();
        final String deck = new RqDeck(this.base, req).deck().name();
        decks.delete(deck);
        return new RsForward(
            new RsFlash(
                String.format("deck \"%s\" deleted", deck)
            )
        );
    }

}
