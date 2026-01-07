/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import com.google.common.collect.Iterables;
import com.jcabi.aspects.Tv;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.cockpit.RsPage;
import java.io.IOException;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
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
import org.xembly.Xembler;

/**
 * Deck.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkIndex implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkIndex(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final Deck deck = new RqDeck(this.base, req).deck();
        final Href home = new Href("/d").path(deck.name());
        final PrettyTime pretty = new PrettyTime();
        return new RsPage(
            "/xsl/deck.xsl",
            this.base,
            req,
            new XeAppend(
                "deck",
                new XeDirectives(
                    Directives.copyOf(new Deck.Smart(deck).xml().node())
                ),
                new XeChain(
                    new XeLink("open", home.path("open")),
                    new XeLink("help", home.path("help")),
                    new XeLink("command", home.path("command"))
                )
            ),
            new XeAppend(
                "events",
                new XeTransform<>(
                    Iterables.limit(
                        deck.events().iterate(Long.MAX_VALUE),
                        Tv.TWENTY
                    ),
                    new XeTransform.Func<String>() {
                        @Override
                        public XeSource transform(final String txt) {
                            final String[] parts = txt.split("\n", Tv.THREE);
                            final long msec = Long.parseLong(parts[1]);
                            return new XeDirectives(
                                new Directives().add("event")
                                    .attr("head", Xembler.escape(parts[0]))
                                    .attr("msec", Long.toString(msec))
                                    .attr("ago", pretty.format(new Date(msec)))
                                    .set(Xembler.escape(parts[2]))
                            );
                        }
                    }
                )
            )
        );
    }

}
