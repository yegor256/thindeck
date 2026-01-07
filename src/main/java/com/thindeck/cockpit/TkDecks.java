/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Href;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeLink;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeTransform;
import org.xembly.Directives;

/**
 * Decks.
 *
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkDecks implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkDecks(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        return new RsPage(
            "/xsl/decks.xsl",
            this.base,
            req,
            new XeLink("add", "/add"),
            new XeAppend(
                "items",
                new XeTransform<>(
                    new RqUser(req, this.base).get().decks().iterate(),
                    new XeTransform.Func<Deck>() {
                        @Override
                        public XeSource transform(final Deck deck)
                            throws IOException {
                            return TkDecks.source(deck);
                        }
                    }
                )
            )
        );
    }

    /**
     * Deck as Xembly source.
     * @param deck The deck
     * @return Source
     * @throws IOException If fails
     */
    private static XeSource source(final Deck deck) throws IOException {
        final Href home = new Href("/d");
        return new XeAppend(
            "item",
            new XeDirectives(
                Directives.copyOf(new Deck.Smart(deck).xml().inner())
            ),
            new XeChain(
                new XeLink("open", home.path(deck.name())),
                new XeLink("delete", home.path(deck.name()).path("delete"))
            )
        );
    }

}
