/**
 * Copyright (c) 2014-2017, Thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the thindeck.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * @author Yegor Bugayenko (yegor@teamed.io)
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
