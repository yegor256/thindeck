/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.thindeck.api.Base;
import com.thindeck.api.Decks;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqForm;

/**
 * Add deck.
 *
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkAddDeck implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkAddDeck(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final RqForm.Smart form = new RqForm.Smart(new RqForm.Base(req));
        final Decks decks = new RqUser(req, this.base).get().decks();
        final String name = form.single("name");
        decks.add(name);
        return new RsForward(
            new RsFlash(String.format("deck \"%s\" added", name))
        );
    }

}
