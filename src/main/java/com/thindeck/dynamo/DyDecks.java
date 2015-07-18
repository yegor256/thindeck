/**
 * Copyright (c) 2014-2015, Thindeck.com
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
package com.thindeck.dynamo;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import java.io.IOException;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link com.thindeck.api.Decks}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 */
@ToString
@Immutable
@EqualsAndHashCode(of = { "region", "user" })
final class DyDecks implements Decks {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Name of the owner.
     */
    private final transient String user;

    /**
     * Ctor.
     * @param reg Region
     * @param usr URN
     */
    DyDecks(final Region reg, final String usr) {
        this.region = reg;
        this.user = usr;
    }

    @Override
    public Deck get(final String name) {
        return new DyDeck(
            this.region, this.user, name
        );
    }

    @Override
    public void add(final String name) throws IOException {
        if (!name.matches("[a-z]{3,12}")) {
            throw new IllegalStateException(
                String.format(
                    "invalid deck name '%s', must be 3-12 English letters",
                    name
                )
            );
        }
        this.region.table(DyDeck.TBL).put(
            new Attributes()
                .with(DyDeck.HASH, this.user)
                .with(DyDeck.RANGE, name)
                .with(DyDeck.ATTR_UPDATED, System.currentTimeMillis())
                .with(DyDeck.ATTR_MEMO, "<deck/>")
        );
    }

    @Override
    public void delete(final String name) {
        final Iterator<Item> items = this.region.table(DyDeck.TBL)
            .frame()
            .through(new QueryValve().withLimit(1))
            .where(DyDeck.HASH, this.user)
            .where(DyDeck.RANGE, name)
            .iterator();
        items.next();
        items.remove();
    }

    @Override
    public Iterable<Deck> iterate() {
        return Iterables.transform(
            this.region.table(DyDeck.TBL)
                .frame()
                .through(new QueryValve())
                .where(DyDeck.HASH, this.user),
            new Function<Item, Deck>() {
                @Override
                public Deck apply(final Item input) {
                    try {
                        return DyDecks.this.get(input.get(DyDeck.RANGE).getS());
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }

}

