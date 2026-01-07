/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import java.net.HttpURLConnection;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkWrap;
import org.takes.facets.fork.Fork;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;
import org.takes.facets.forward.RsForward;
import org.takes.misc.Opt;
import org.takes.rq.RqHref;
import org.takes.rq.RqWithHeader;

/**
 * Deck fork.
 *
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class FkDeck extends FkWrap {

    /**
     * Ctor.
     * @param regex Regular expression
     * @param take Take
     */
    FkDeck(final String regex, final Take take) {
        super(
            new Fork() {
                @Override
                public Opt<Response> route(final Request req)
                    throws Exception {
                    return FkDeck.route(regex, take, req);
                }
            }
        );
    }

    /**
     * Route.
     * @param regex Regular expression
     * @param take Take
     * @param req Request
     * @return Response or empty
     * @throws Exception If fails
     */
    private static Opt<Response> route(final String regex, final Take take,
        final Request req) throws Exception {
        return new FkRegex(
            String.format("/d/([a-z\\-]+)%s", regex),
            new TkRegex() {
                @Override
                public Response act(final RqRegex rreq) throws Exception {
                    final String name = rreq.matcher().group(1);
                    return FkDeck.redirect(name, take).act(
                        new RqWithHeader(rreq, "X-Thindeck-Deck", name)
                    );
                }
            }
        ).route(req);
    }

    /**
     * Redirect to the bout.
     * @param deck Deck name
     * @param take Take
     * @return New take
     */
    private static Take redirect(final String deck, final Take take) {
        return new Take() {
            @Override
            public Response act(final Request req) throws Exception {
                try {
                    return take.act(req);
                } catch (final RsForward ex) {
                    if (ex.code() == HttpURLConnection.HTTP_SEE_OTHER) {
                        throw new RsForward(
                            ex,
                            new RqHref.Smart(
                                new RqHref.Base(req)
                            ).home().path("d").path(deck)
                        );
                    }
                    throw ex;
                }
            }
        };
    }

}
