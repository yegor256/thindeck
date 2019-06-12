/**
 * Copyright (c) 2014-2019, Thindeck.com
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
